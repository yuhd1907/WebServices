package ra.edu.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean isActive;
}
