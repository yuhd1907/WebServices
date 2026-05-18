package ra.edu.service;

import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserRoleRequest;
import ra.edu.dto.request.UserStatusRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.RoleName;

import java.util.List;

public interface UserService {
    // ADMIN: Lấy danh sách tất cả người dùng (có thể lọc)
    List<UserProfileResponse> getAllUsers(RoleName role);
    // ADMIN: Xem chi tiết 1 người dùng
    UserProfileResponse getUserById(Long userId);
    // ADMIN: Tạo tài khoản người dùng mới (chỉ tài khoản, không cần thêm profile)
    UserProfileResponse createUser(UserCreateRequest request);
    // ADMIN: Cập nhật thông tin cơ bản người dùng
    UserProfileResponse updateUser(Long userId, UserUpdateRequest request);
    // ADMIN: Cập nhật trạng thái kích hoạt (is_active)
    void updateUserStatus(Long userId, UserStatusRequest request);
    // ADMIN: Cập nhật vai trò (role)
    void updateUserRole(Long userId, UserRoleRequest request);
    // ADMIN: Xóa người dùng
    void deleteUser(Long userId);
}
