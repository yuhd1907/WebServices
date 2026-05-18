package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * ADMIN dùng để tạo hồ sơ giáo viên hướng dẫn (liên kết với User có role MENTOR)
 */
@Getter
@Setter
public class MentorCreateRequest {
    @NotNull(message = "User ID không được để trống")
    private Long userId;

    private String department;
    private String academicRank;
}
