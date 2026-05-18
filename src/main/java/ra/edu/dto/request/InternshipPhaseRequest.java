package ra.edu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InternshipPhaseRequest {

    @NotBlank(message = "Tên giai đoạn thực tập không được để trống")
    @Size(max = 100, message = "Tên giai đoạn không được vượt quá 100 ký tự")
    private String phaseName;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    private String description;
}
