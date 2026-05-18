package ra.edu.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EvaluationCriterionRequest {

    @NotBlank(message = "Tên tiêu chí không được để trống")
    @Size(max = 200, message = "Tên tiêu chí không được vượt quá 200 ký tự")
    private String criterionName;

    private String description;

    @NotNull(message = "Điểm tối đa không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Điểm tối đa phải lớn hơn 0")
    private BigDecimal maxScore;
}
