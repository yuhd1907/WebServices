package ra.edu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import ra.edu.util.AppConstants;

import java.time.LocalDate;

/**
 * ADMIN dùng để tạo hồ sơ sinh viên (liên kết với User có role STUDENT)
 */
@Getter
@Setter
public class StudentCreateRequest {
    @NotNull(message = "User ID không được để trống")
    private Long userId;

    @NotBlank(message = "Mã sinh viên không được để trống")
    @Pattern(regexp = AppConstants.STUDENT_CODE_REGEX, message = "Mã sinh viên không đúng định dạng (VD: K20CNTT001)")
    private String studentCode;

    private String major;
    private String className;
    private LocalDate dateOfBirth;
    private String address;
}
