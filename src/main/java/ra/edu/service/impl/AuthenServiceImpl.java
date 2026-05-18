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
        // Kiểm tra dữ liệu trùng lặp
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Tên đăng nhập đã được sử dụng");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email đã được sử dụng");
        }
        if (userRepository.existsByPhoneNumber(request.getPhone())) {
            throw new ConflictException("Số điện thoại đã được đăng kí");
        }

        // tạo mới user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setFullName(request.getFullName());
        user.setRole(roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new BadRequestException("Vai trò không hợp lệ: " + request.getRoleName())));
        // Lưu user
        userRepository.save(user);

        // Nếu role là STUDENT -> tự động tạo bản ghi trong bảng students
        if (request.getRoleName() == RoleName.STUDENT) {
            // Auto-generate mã sinh viên: SV + timestamp
            String studentCode = "SV" + System.currentTimeMillis();
            Student student = Student.builder()
                    .user(user)
                    .studentCode(studentCode)
                    .build();
            studentRepository.save(student);
        }

        return UserMapper.toProfileResponse(user);
    }

    @Override
    public JwtResponse refreshToken(ra.edu.dto.request.RefreshTokenRequest request) {
        throw new UnsupportedOperationException("Tính năng refresh token chưa được triển khai");
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
        return UserMapper.toProfileResponse(user);
    }
}

