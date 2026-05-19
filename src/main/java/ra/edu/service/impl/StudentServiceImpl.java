package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.StudentResponse;
import ra.edu.dto.response.StudentSummaryResponse;
import ra.edu.entity.Mentor;
import ra.edu.entity.Student;
import ra.edu.entity.User;
import ra.edu.entity.RoleName;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.mapper.StudentMapper;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.StudentService;
import ra.edu.util.AppConstants;

import java.util.List;
import java.util.stream.Collectors;

import ra.edu.repository.InternshipAssignmentRepository;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final InternshipAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<StudentSummaryResponse> getStudents(String username, String role) {
        List<Student> students;

        if (AppConstants.ROLE_ADMIN.equals(role)) {
            // ADMIN lấy toàn bộ danh sách sinh viên
            students = studentRepository.findAll();
        } else {
            // MENTOR chỉ lấy sinh viên được phân công cho mình (trích từ bảng phân công)
            students = assignmentRepository.findByMentor_User_Username(username).stream()
                    .map(ra.edu.entity.InternshipAssignment::getStudent)
                    .distinct()
                    .collect(Collectors.toList());
        }

        return students.stream()
                .map(StudentMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentById(Long studentId, String username, String role) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với id: " + studentId));

        // STUDENT chỉ được xem thông tin của chính mình
        if (AppConstants.ROLE_STUDENT.equals(role)) {
            Student myProfile = studentRepository.findByUser_Username(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ của bạn"));
            if (!myProfile.getStudentId().equals(studentId)) {
                throw new ForbiddenException("Bạn không có quyền xem thông tin của sinh viên khác");
            }
        }

        return StudentMapper.toResponse(student);
    }

    @Override
    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request) {
        // Kiểm tra user tồn tại
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id: " + request.getUserId()));

        // Kiểm tra user phải có role STUDENT
        if (user.getRole().getRoleName() != RoleName.STUDENT) {
            throw new BadRequestException("Người dùng này không có vai trò STUDENT");
        }

        // Kiểm tra user đã có hồ sơ sinh viên chưa
        if (studentRepository.findById(request.getUserId()).isPresent()) {
            throw new ConflictException("Người dùng này đã có hồ sơ sinh viên");
        }

        // Kiểm tra mã sinh viên trùng
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new ConflictException("Mã sinh viên '" + request.getStudentCode() + "' đã tồn tại");
        }

        Student student = Student.builder()
                .user(user)
                .studentCode(request.getStudentCode())
                .major(request.getMajor())
                .className(request.getClassName())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .build();

        studentRepository.save(student);
        return StudentMapper.toResponse(student);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long studentId, StudentUpdateRequest request, String username, String role) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên với id: " + studentId));

        // STUDENT chỉ được cập nhật hồ sơ của chính mình
        if (AppConstants.ROLE_STUDENT.equals(role)) {
            Student myProfile = studentRepository.findByUser_Username(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ của bạn"));
            if (!myProfile.getStudentId().equals(studentId)) {
                throw new ForbiddenException("Bạn không có quyền cập nhật thông tin của sinh viên khác");
            }
        }

        student.setMajor(request.getMajor());
        student.setClassName(request.getClassName());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAddress(request.getAddress());

        studentRepository.save(student);
        return StudentMapper.toResponse(student);
    }
}
