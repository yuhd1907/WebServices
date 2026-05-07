package ra.edu.service;

import ra.edu.dto.request.FormLogin;
import ra.edu.dto.request.FormRegister;
import ra.edu.dto.request.RefreshTokenRequest;
import ra.edu.dto.response.JwtResponse;
import ra.edu.dto.response.UserProfileResponse;

public interface AuthenSevice {
    // Đăng ký (chỉ dành cho admin)
    UserProfileResponse register(FormRegister request);
    // Đăng nhập
    JwtResponse login(FormLogin request);
    // Làm mới access token bằng refresh token
    JwtResponse refreshToken(RefreshTokenRequest request);
    // Lấy profile người dùng hiện tại
    UserProfileResponse getProfile(String username);
}
