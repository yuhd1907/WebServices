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

    // --- Phone & Student Code validation ---
    public static final String PHONE_REGEX = "^[0-9]{10}$";
    public static final String STUDENT_CODE_REGEX = "^[A-Z0-9]{2,10}$"; // Cho phép chữ và số từ 2-10 ký tự

    // --- Prefix ---
    public static final String ROLE_PREFIX = "ROLE_";
}
