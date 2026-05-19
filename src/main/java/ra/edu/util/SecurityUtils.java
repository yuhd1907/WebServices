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

    /**
     * Tiện ích lấy username trực tiếp từ SecurityContext
     */
    public static String getCurrentUsername() {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    /**
     * Tiện ích lấy role name trực tiếp từ SecurityContext (có tiền tố ROLE_)
     */
    public static String getCurrentUserRole() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
    }
}
