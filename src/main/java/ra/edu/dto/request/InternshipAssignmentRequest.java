package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternshipAssignmentRequest {
    @NotNull(message = "ID sinh viên không được để trống")
    private Long studentId;

    @NotNull(message = "ID giáo viên hướng dẫn không được để trống")
    private Long mentorId;

    @NotNull(message = "ID giai đoạn thực tập không được để trống")
    private Long phaseId;
}
