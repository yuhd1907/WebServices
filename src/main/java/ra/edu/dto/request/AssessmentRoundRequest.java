package ra.edu.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AssessmentRoundRequest {

    @NotNull(message = "Phase ID không được để trống")
    private Long phaseId;

    @NotBlank(message = "Tên đợt đánh giá không được để trống")
    @Size(max = 100, message = "Tên đợt đánh giá không được vượt quá 100 ký tự")
    private String roundName;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    private String description;

    private Boolean isActive;

    @Valid
    private List<RoundCriterionRequestItem> criteria;
}
