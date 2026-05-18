package ra.edu.util;

/**
 * Hằng số dùng chung toàn hệ thống
 */
public class AppConstants {

    private AppConstants() {} // Không cho khởi tạo

    // --- Role names ---
    public static final String ROLE_ADMIN   = "ADMIN";
    public static final String ROLE_MENTOR  = "MENTOR";
    public static final String ROLE_STUDENT = "STUDENT";

    public static final String PHONE_REGEX = "^[0-9]{10}$";

    // --- Prefix ---
    public static final String ROLE_PREFIX = "ROLE_";
}
