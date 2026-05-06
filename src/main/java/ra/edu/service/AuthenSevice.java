package ra.edu.service;

import ra.edu.dto.request.FormLogin;
import ra.edu.dto.request.FormRegister;
import ra.edu.dto.response.JwtResponse;
import ra.edu.dto.response.UserProfileResponse;

public interface AuthenSevice {
    // Đăng nhập
    JwtResponse login(FormLogin request);
    // Đăng ký
    void register(FormRegister request);
    // Lấy profile người dùng hiện tại
    UserProfileResponse getProfile(String username);
}
