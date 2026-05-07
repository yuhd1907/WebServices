package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.FormLogin;
import ra.edu.dto.request.FormRegister;
import ra.edu.dto.request.RefreshTokenRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.JwtResponse;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.service.AuthenSevice;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenSevice authenService;

    /**
     * POST /api/auth/register - Đăng ký tài khoản mới (Chỉ dành cho ADMIN)
     * Quyền: ADMIN
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserProfileResponse>> register(@Valid @RequestBody FormRegister request) {
        UserProfileResponse newUser = authenService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký tài khoản thành công", newUser));
    }

    /**
     * POST /api/auth/login - Đăng nhập và nhận JWT
     * Quyền: PUBLIC
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody FormLogin request) {
        JwtResponse jwtResponse = authenService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", jwtResponse));
    }

    /**
     * POST /api/auth/refresh-token - Làm mới access token khi hết hạn (1 giờ)
     * Quyền: PUBLIC (client gửi refresh token)
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        JwtResponse jwtResponse = authenService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Làm mới token thành công", jwtResponse));
    }

    /**
     * GET /api/auth/me - Lấy thông tin hồ sơ người dùng hiện tại
     * Quyền: ADMIN, MENTOR, STUDENT (cần Bearer Token)
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserProfileResponse profile = authenService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin hồ sơ thành công", profile));
    }
}
