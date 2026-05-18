package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserRoleRequest;
import ra.edu.dto.request.UserStatusRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.RoleName;
import ra.edu.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    /**
     * GET /api/users - Lấy danh sách tất cả người dùng trong hệ thống (có thể lọc theo vai trò)
     * Quyền: ADMIN
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getAllUsers(
            @RequestParam(value = "role", required = false) RoleName role) {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách người dùng thành công",
                userService.getAllUsers(role)
        ));
    }

    /**
     * GET /api/users/{user_id} - Lấy thông tin chi tiết một người dùng theo ID
     * Quyền: ADMIN
     */
    @GetMapping("/{user_id}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserById(
            @PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy thông tin người dùng thành công",
                userService.getUserById(userId)
        ));
    }

    /**
     * POST /api/users - ADMIN tạo tài khoản người dùng mới (chỉ tài khoản + role)
     * Quyền: ADMIN
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> createUser(
            @Valid @RequestBody UserCreateRequest request) {
        UserProfileResponse newUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo tài khoản người dùng thành công", newUser));
    }

    /**
     * PUT /api/users/{user_id} - Cập nhật thông tin cơ bản của người dùng
     * Quyền: ADMIN
     */
    @PutMapping("/{user_id}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUser(
            @PathVariable("user_id") Long userId,
            @Valid @RequestBody UserUpdateRequest request) {
        UserProfileResponse updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin người dùng thành công", updatedUser));
    }

    /**
     * PUT /api/users/{user_id}/status - Cập nhật trạng thái kích hoạt (is_active)
     * Quyền: ADMIN
     */
    @PutMapping("/{user_id}/status")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(
            @PathVariable("user_id") Long userId,
            @Valid @RequestBody UserStatusRequest request) {
        userService.updateUserStatus(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái người dùng thành công"));
    }

    /**
     * PUT /api/users/{user_id}/role - Cập nhật vai trò (role) của người dùng
     * Quyền: ADMIN
     */
    @PutMapping("/{user_id}/role")
    public ResponseEntity<ApiResponse<Void>> updateUserRole(
            @PathVariable("user_id") Long userId,
            @Valid @RequestBody UserRoleRequest request) {
        userService.updateUserRole(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật vai trò người dùng thành công"));
    }

    /**
     * DELETE /api/users/{user_id} - Xóa người dùng khỏi hệ thống
     * Quyền: ADMIN
     */
    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("user_id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Xóa người dùng thành công"));
    }
}
