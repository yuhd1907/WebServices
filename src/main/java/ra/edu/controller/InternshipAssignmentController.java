package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.InternshipAssignmentRequest;
import ra.edu.dto.request.InternshipAssignmentStatusRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.InternshipAssignmentResponse;
import ra.edu.service.InternshipAssignmentService;

import java.util.List;

@RestController
@RequestMapping("/api/internship_assignments")
@RequiredArgsConstructor
public class InternshipAssignmentController {

    private final InternshipAssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InternshipAssignmentResponse>>> getAllAssignments() {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách phân công thực tập thành công",
                assignmentService.getAllAssignments()
        ));
    }

    @GetMapping("/{assignment_id}")
    public ResponseEntity<ApiResponse<InternshipAssignmentResponse>> getAssignmentById(
            @PathVariable("assignment_id") Long assignmentId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy chi tiết phân công thực tập thành công",
                assignmentService.getAssignmentById(assignmentId)
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InternshipAssignmentResponse>> createAssignment(
            @Valid @RequestBody InternshipAssignmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Tạo phân công thực tập mới thành công",
                assignmentService.createAssignment(request)
        ));
    }

    @PutMapping("/{assignment_id}/status")
    public ResponseEntity<ApiResponse<InternshipAssignmentResponse>> updateAssignmentStatus(
            @PathVariable("assignment_id") Long assignmentId,
            @Valid @RequestBody InternshipAssignmentStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Cập nhật trạng thái phân công thành công",
                assignmentService.updateAssignmentStatus(assignmentId, request)
        ));
    }
}
