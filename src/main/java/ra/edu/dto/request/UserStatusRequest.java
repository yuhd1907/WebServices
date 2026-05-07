package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusRequest {
    @NotNull(message = "Trạng thái kích hoạt không được để trống")
    private Boolean isActive;
}
