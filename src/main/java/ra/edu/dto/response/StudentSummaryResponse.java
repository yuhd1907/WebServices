package ra.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO rút gọn cho danh sách sinh viên - chỉ trả về id, tên và code
 */
@Getter
@Setter
@Builder
public class StudentSummaryResponse {
    private Long studentId;
    private String studentCode;
    private String fullName;
}
