package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import ra.edu.service.AuthenSevice;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.edu.config.jwt.JwtService;
import ra.edu.dto.request.FormLogin;
import ra.edu.dto.request.FormRegister;
import ra.edu.dto.request.RefreshTokenRequest;
import ra.edu.dto.response.JwtResponse;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.User;
import ra.edu.entity.Student;
import ra.edu.entity.RoleName;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.mapper.UserMapper;
import ra.edu.repository.RoleRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.repository.UserRepository;
import ra.edu.util.AppConstants;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenSevice {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserProfileResponse register(FormRegister request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Tên đăng nhập đã được sử dụng");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email đã được sử dụng");
        }
        if (userRepository.existsByPhoneNumber(request.getPhone())) {
            throw new ConflictException("Số điện thoại đã được đăng kí");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setFullName(request.getFullName());
        // Role mặc định là ADMIN, không cho phép tự chọn role
        user.setRole(roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new BadRequestException("Không tìm thấy vai trò ADMIN trong hệ thống")));
        
        userRepository.save(user);
        return UserMapper.toProfileResponse(user);
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
        User user = userRepository.loadUserByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Không tìm thấy người dùng"));
        
        org.springframework.security.core.userdetails.UserDetails userDetails =
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return JwtResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expirationDate(jwtService.getExpirationDate(accessToken))
                .build();
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        // Validate refresh token
        if (!jwtService.validateToken(refreshToken)) {
            throw new BadRequestException("Refresh token không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại.");
        }
        String username = jwtService.getUsernameFromRefreshToken(refreshToken);
        User user = userRepository.loadUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + username));
        // Cấp access token mới
        String newAccessToken = jwtService.generateAccessToken(username);
        return JwtResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // Giữ nguyên refresh token cũ
                .expirationDate(jwtService.getExpirationDate(newAccessToken))
                .build();
    }

    @Override
    public UserProfileResponse getProfile(String username) {
        User user = userRepository.loadUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + username));
        return UserMapper.toProfileResponse(user);
    }
}

