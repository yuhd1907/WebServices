package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.InternshipPhaseRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.InternshipPhaseResponse;
import ra.edu.service.InternshipPhaseService;

import java.util.List;

@RestController
@RequestMapping("/api/internship_phases")
@RequiredArgsConstructor
public class InternshipPhaseController {

    private final InternshipPhaseService phaseService;

    /**
     * GET /api/internship_phases - Lấy danh sách tất cả giai đoạn thực tập
     * Quyền: ADMIN, MENTOR, STUDENT
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InternshipPhaseResponse>>> getAllPhases() {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách giai đoạn thực tập thành công",
                phaseService.getAllPhases()
        ));
    }

    /**
     * GET /api/internship_phases/{phase_id} - Lấy thông tin chi tiết giai đoạn thực tập theo ID
     * Quyền: ADMIN, MENTOR, STUDENT
     */
    @GetMapping("/{phase_id}")
    public ResponseEntity<ApiResponse<InternshipPhaseResponse>> getPhaseById(
            @PathVariable("phase_id") Long phaseId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy thông tin giai đoạn thực tập thành công",
                phaseService.getPhaseById(phaseId)
        ));
    }

    /**
     * POST /api/internship_phases - Tạo mới giai đoạn thực tập
     * Quyền: ADMIN
     */
    @PostMapping
    public ResponseEntity<ApiResponse<InternshipPhaseResponse>> createPhase(
            @Valid @RequestBody InternshipPhaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Tạo giai đoạn thực tập thành công",
                phaseService.createPhase(request)
        ));
    }

    /**
     * PUT /api/internship_phases/{phase_id} - Cập nhật thông tin giai đoạn thực tập
     * Quyền: ADMIN
     */
    @PutMapping("/{phase_id}")
    public ResponseEntity<ApiResponse<InternshipPhaseResponse>> updatePhase(
            @PathVariable("phase_id") Long phaseId,
            @Valid @RequestBody InternshipPhaseRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Cập nhật giai đoạn thực tập thành công",
                phaseService.updatePhase(phaseId, request)
        ));
    }

    /**
     * DELETE /api/internship_phases/{phase_id} - Xóa giai đoạn thực tập
     * Quyền: ADMIN
     */
    @DeleteMapping("/{phase_id}")
    public ResponseEntity<ApiResponse<Void>> deletePhase(
            @PathVariable("phase_id") Long phaseId) {
        phaseService.deletePhase(phaseId);
        return ResponseEntity.ok(ApiResponse.success("Xóa giai đoạn thực tập thành công"));
    }
}
