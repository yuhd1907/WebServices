package ra.edu.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * ADMIN hoặc STUDENT dùng để cập nhật hồ sơ sinh viên
 */
@Getter
@Setter
public class StudentUpdateRequest {
    private String major;
    private String className;
    private LocalDate dateOfBirth;
    private String address;
}
