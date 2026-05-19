package ra.edu.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssessmentResultRequest {
    @NotNull(message = "ID phân công thực tập không được để trống")
    private Long assignmentId;

    @NotNull(message = "ID đợt đánh giá không được để trống")
    private Long roundId;

    @NotNull(message = "ID tiêu chí đánh giá không được để trống")
    private Long criterionId;

    @NotNull(message = "Điểm không được để trống")
    @DecimalMin(value = "0.0", message = "Điểm phải lớn hơn hoặc bằng 0")
    private Double score;

    private String comments;
}
