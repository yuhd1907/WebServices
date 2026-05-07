package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.StudentResponse;
import ra.edu.dto.response.StudentSummaryResponse;
import ra.edu.service.StudentService;
import ra.edu.util.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * GET /api/students - Lấy danh sách sinh viên (chỉ id, tên, code)
     * ADMIN: tất cả | MENTOR: chỉ sinh viên được phân công
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentSummaryResponse>>> getStudents(
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = SecurityUtils.extractUsername(userDetails);
        String role     = SecurityUtils.extractRole(userDetails);
        List<StudentSummaryResponse> students = studentService.getStudents(username, role);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sinh viên thành công", students));
    }

    /**
     * GET /api/students/{student_id} - Xem chi tiết sinh viên
     * ADMIN, MENTOR: xem tất cả | STUDENT: chỉ xem của mình
     */
    @GetMapping("/{student_id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(
            @PathVariable("student_id") Long studentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = SecurityUtils.extractUsername(userDetails);
        String role     = SecurityUtils.extractRole(userDetails);
        StudentResponse student = studentService.getStudentById(studentId, username, role);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin sinh viên thành công", student));
    }

    /**
     * POST /api/students - Tạo hồ sơ sinh viên mới
     * Quyền: ADMIN
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(
            @Valid @RequestBody StudentCreateRequest request) {
        StudentResponse student = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo hồ sơ sinh viên thành công", student));
    }

    /**
     * PUT /api/students/{student_id} - Cập nhật thông tin chi tiết sinh viên
     * ADMIN: cập nhật tất cả | STUDENT: chỉ cập nhật của mình
     */
    @PutMapping("/{student_id}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable("student_id") Long studentId,
            @RequestBody StudentUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = SecurityUtils.extractUsername(userDetails);
        String role     = SecurityUtils.extractRole(userDetails);
        StudentResponse student = studentService.updateStudent(studentId, request, username, role);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin sinh viên thành công", student));
    }
}