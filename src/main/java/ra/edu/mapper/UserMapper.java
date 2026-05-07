package ra.edu.mapper;

import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.User;

/**
 * Mapper chuyển đổi giữa User entity và các DTO liên quan
 */
public class UserMapper {

    private UserMapper() {}

    /**
     * Chuyển User entity -> UserProfileResponse DTO
     */
    public static UserProfileResponse toProfileResponse(User user) {
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().getRoleName().name())
                .isActive(user.getIsActive())
                .build();
    }
}
