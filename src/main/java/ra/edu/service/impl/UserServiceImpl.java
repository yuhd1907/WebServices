package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserRoleRequest;
import ra.edu.dto.request.UserStatusRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.response.UserProfileResponse;
import ra.edu.entity.Role;
import ra.edu.entity.RoleName;
import ra.edu.entity.User;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.mapper.UserMapper;
import ra.edu.repository.RoleRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserProfileResponse> getAllUsers(RoleName role) {
        List<User> users;
        if (role != null) {
            // Lọc theo role nếu có truyền query param
            users = userRepository.findAllByRole_RoleName(role);
        } else {
            users = userRepository.findAll();
        }
        return users.stream()
                .map(UserMapper::toProfileResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id: " + userId));
        return UserMapper.toProfileResponse(user);
    }

    @Override
    public UserProfileResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Tên đăng nhập đã được sử dụng");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email đã được sử dụng");
        }
        if (request.getPhone() != null && userRepository.existsByPhoneNumber(request.getPhone())) {
            throw new ConflictException("Số điện thoại đã được đăng ký");
        }

        Role role = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new BadRequestException("Vai trò không hợp lệ: " + request.getRoleName()));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setFullName(request.getFullName());
        user.setRole(role);

        userRepository.save(user);
        return UserMapper.toProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id: " + userId));

        // Validate email trùng lặp nếu đổi email
        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Email đã được sử dụng");
            }
            user.setEmail(request.getEmail());
        }

        // Validate số điện thoại trùng lặp nếu đổi
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new ConflictException("Số điện thoại đã được đăng ký");
            }
            user.setPhoneNumber(request.getPhoneNumber());
        }

        user.setFullName(request.getFullName());
        userRepository.save(user);
        return UserMapper.toProfileResponse(user);
    }

    @Override
    public void updateUserStatus(Long userId, UserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id: " + userId));

        user.setIsActive(request.getIsActive());
        userRepository.save(user);
    }

    @Override
    public void updateUserRole(Long userId, UserRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id: " + userId));

        // ADMIN không được đổi quyền của một ADMIN khác
        if (user.getRole().getRoleName() == RoleName.ADMIN) {
            throw new BadRequestException("Không thể thay đổi vai trò của một quản trị viên (ADMIN) khác");
        }

        Role role = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new BadRequestException("Vai trò không hợp lệ: " + request.getRoleName()));

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id: " + userId));

        // Cấm xóa ADMIN
        if (user.getRole().getRoleName() == RoleName.ADMIN) {
            throw new BadRequestException("Không thể xóa quản trị viên (ADMIN)");
        }

        userRepository.delete(user);
    }
}
