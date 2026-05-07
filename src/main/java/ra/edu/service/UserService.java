package ra.edu.service;

import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserRoleRequest;
import ra.edu.dto.request.UserStatusRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.RoleName;

import java.util.List;

public interface UserService {
    // Lấy danh sách tất cả users (lọc theo role nếu có)
    List<UserProfileResponse> getAllUsers(RoleName role);

    // Lấy chi tiết 1 user
    UserProfileResponse getUserById(Long userId);

    // ADMIN tạo tài khoản mới (có thể chọn role MENTOR/STUDENT)
    UserProfileResponse createUser(UserCreateRequest request);

    // Cập nhật thông tin cơ bản
    UserProfileResponse updateUser(Long userId, UserUpdateRequest request);

    // Cập nhật trạng thái is_active
    void updateUserStatus(Long userId, UserStatusRequest request);

    // Cập nhật role
    void updateUserRole(Long userId, UserRoleRequest request);

    // Xóa user
    void deleteUser(Long userId);
}
