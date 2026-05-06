package ra.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.edu.config.jwt.JwtService;
import ra.edu.dto.request.FormLogin;
import ra.edu.dto.request.FormRegister;
import ra.edu.dto.response.JwtResponse;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.User;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.repository.RoleRepository;
import ra.edu.repository.UserRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenSevice {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void register(FormRegister request) {
        // tạo mới user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setFullName(request.getFullName());
        user.setRole(roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new BadRequestException("Vai trò không hợp lệ: " + request.getRoleName())));
        // lưu
        userRepository.save(user);
    }

    @Override
    public JwtResponse login(FormLogin request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()
            ));
        } catch (Exception e) {
            throw new BadRequestException("Tên đăng nhập hoặc mật khẩu không chính xác");
        }
        User user = userRepository.loadUserByUsername(request.getUsername()).orElseThrow();
        return JwtResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .accessToken(jwtService.generateAccessToken(user.getUsername()))
                .expirationDate(new Date(new Date().getTime() + 15 * 60 * 1000))
                .refreshToken(null)
                .build();
    }

    @Override
    public UserProfileResponse getProfile(String username) {
        User user = userRepository.loadUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + username));
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

