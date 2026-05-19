package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ra.edu.entity.AssignmentStatus;

@Getter
@Setter
public class InternshipAssignmentStatusRequest {
    @NotNull(message = "Trạng thái không được để trống")
    private AssignmentStatus status;
}
