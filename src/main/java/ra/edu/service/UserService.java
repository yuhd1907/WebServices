package ra.edu.service;

import ra.edu.dto.request.UserRoleRequest;
import ra.edu.dto.request.UserStatusRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.response.UserProfileResponse;

public interface UserService {
    UserProfileResponse updateUser(Long userId, UserUpdateRequest request);
    void updateUserStatus(Long userId, UserStatusRequest request);
    void updateUserRole(Long userId, UserRoleRequest request);
    void deleteUser(Long userId);
}
