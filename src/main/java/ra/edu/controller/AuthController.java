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
     * POST /api/auth/register - Đăng ký tài khoản mới
     * Quyền: PUBLIC
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody FormRegister request) {
        authenService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký tài khoản thành công"));
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
