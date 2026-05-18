package ra.edu.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * ADMIN hoặc MENTOR dùng để cập nhật hồ sơ giáo viên hướng dẫn
 */
@Getter
@Setter
public class MentorUpdateRequest {
    private String department;
    private String academicRank;
}
