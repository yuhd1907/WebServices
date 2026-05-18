package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.edu.dto.request.AssignStudentsRequest;
import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.MentorResponse;
import ra.edu.entity.Mentor;
import ra.edu.entity.RoleName;
import ra.edu.entity.Student;
import ra.edu.entity.User;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.mapper.MentorMapper;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.MentorService;
import ra.edu.util.AppConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;
    private final UserRepository   userRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<MentorResponse> getMentors() {
        return mentorRepository.findAll().stream()
                .map(MentorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MentorResponse getMentorById(Long mentorId) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên với id: " + mentorId));
        return MentorMapper.toResponse(mentor);
    }

    @Override
    public MentorResponse createMentor(MentorCreateRequest request) {
        // Kiểm tra user tồn tại
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id: " + request.getUserId()));

        // Kiểm tra user phải có role MENTOR
        if (user.getRole().getRoleName() != RoleName.MENTOR) {
            throw new BadRequestException("Người dùng này không có vai trò MENTOR");
        }

        // Kiểm tra user đã có hồ sơ mentor chưa
        if (mentorRepository.findById(request.getUserId()).isPresent()) {
            throw new ConflictException("Người dùng này đã có hồ sơ giáo viên hướng dẫn");
        }

        Mentor mentor = Mentor.builder()
                .user(user)
                .department(request.getDepartment())
                .academicRank(request.getAcademicRank())
                .build();

        mentorRepository.save(mentor);
        return MentorMapper.toResponse(mentor);
    }

    @Override
    public MentorResponse updateMentor(Long mentorId, MentorUpdateRequest request, String username, String role) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên với id: " + mentorId));

        // MENTOR chỉ được cập nhật hồ sơ của chính mình
        if (AppConstants.ROLE_MENTOR.equals(role)) {
            Mentor myProfile = mentorRepository.findByUser_Username(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ của bạn"));
            if (!myProfile.getMentorId().equals(mentorId)) {
                throw new ForbiddenException("Bạn không có quyền chỉnh sửa hồ sơ của giáo viên khác");
            }
        }

        mentor.setDepartment(request.getDepartment());
        mentor.setAcademicRank(request.getAcademicRank());

        mentorRepository.save(mentor);
        return MentorMapper.toResponse(mentor);
    }

    @Override
    public void assignStudentsToMentor(Long mentorId, AssignStudentsRequest request) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên hướng dẫn với id: " + mentorId));

        Set<Student> studentsToAssign = new HashSet<>();
        for (Long studentId : request.getStudentIds()) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với id: " + studentId));
            studentsToAssign.add(student);
        }

        // Gán danh sách sinh viên được phân công vào Mentor
        mentor.setAssignedStudents(studentsToAssign);
        mentorRepository.save(mentor);
    }
}
