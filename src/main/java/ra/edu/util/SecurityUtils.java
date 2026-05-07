package ra.edu.util;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Helper class để trích xuất thông tin từ SecurityContext
 */
public class SecurityUtils {

    private SecurityUtils() {}

    /**
     * Lấy role thuần (bỏ tiền tố ROLE_) từ UserDetails hiện tại
     * Ví dụ: "ROLE_ADMIN" -> "ADMIN"
     */
    public static String extractRole(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace(AppConstants.ROLE_PREFIX, "");
    }

    /**
     * Lấy username từ UserDetails
     */
    public static String extractUsername(UserDetails userDetails) {
        return userDetails.getUsername();
    }
}
