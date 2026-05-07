package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ra.edu.entity.RoleName;

@Getter
@Setter
public class UserRoleRequest {
    @NotNull(message = "Vai trò không được để trống")
    private RoleName roleName;
}
