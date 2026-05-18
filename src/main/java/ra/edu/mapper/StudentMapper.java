package ra.edu.mapper;

import ra.edu.dto.response.StudentResponse;
import ra.edu.entity.Student;

/**
 * Mapper chuyển đổi giữa Student entity và các DTO liên quan
 */
public class StudentMapper {

    private StudentMapper() {}

    /**
     * Chuyển Student entity -> StudentResponse DTO
     */
    public static StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .studentCode(student.getStudentCode())
                .fullName(student.getUser().getFullName())
                .email(student.getUser().getEmail())
                .phoneNumber(student.getUser().getPhoneNumber())
                .major(student.getMajor())
                .className(student.getClassName())
                .dateOfBirth(student.getDateOfBirth())
                .address(student.getAddress())
                .isActive(student.getUser().getIsActive())
                .build();
    }
}
