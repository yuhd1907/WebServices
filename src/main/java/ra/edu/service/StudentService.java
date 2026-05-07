package ra.edu.service;

import ra.edu.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {
    /**
     * Lấy danh sách sinh viên:
     * - ADMIN: toàn bộ sinh viên
     * - MENTOR: chỉ sinh viên được phân công cho mentor đó
     */
    List<StudentResponse> getStudents(String username, String role);
}
