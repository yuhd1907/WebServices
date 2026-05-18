package ra.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.StudentResponse;
import ra.edu.service.StudentService;
import ra.edu.util.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * GET /api/students - Lấy danh sách sinh viên
     * - ADMIN: Toàn bộ sinh viên
     * - MENTOR: Chỉ sinh viên được phân công
     * Quyền: ADMIN, MENTOR
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudents(
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = SecurityUtils.extractUsername(userDetails);
        String role     = SecurityUtils.extractRole(userDetails);

        List<StudentResponse> students = studentService.getStudents(username, role);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sinh viên thành công", students));
    }
}
