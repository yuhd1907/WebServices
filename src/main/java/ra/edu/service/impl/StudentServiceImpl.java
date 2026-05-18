package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.edu.dto.response.StudentResponse;
import ra.edu.entity.Mentor;
import ra.edu.entity.Student;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.mapper.StudentMapper;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.service.StudentService;
import ra.edu.util.AppConstants;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;

    @Override
    public List<StudentResponse> getStudents(String username, String role) {
        List<Student> students;

        if (AppConstants.ROLE_ADMIN.equals(role)) {
            // ADMIN lấy toàn bộ danh sách sinh viên
            students = studentRepository.findAll();
        } else {
            // MENTOR chỉ lấy sinh viên được phân công
            Mentor mentor = mentorRepository.findByUser_Username(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin mentor: " + username));
            students = List.copyOf(mentor.getAssignedStudents());
        }

        return students.stream()
                .map(StudentMapper::toResponse)
                .collect(Collectors.toList());
    }
}
