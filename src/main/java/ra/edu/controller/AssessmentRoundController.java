package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.AssessmentRoundRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.AssessmentRoundResponse;
import ra.edu.service.AssessmentRoundService;

import java.util.List;

@RestController
@RequestMapping("/api/assessment_rounds")
@RequiredArgsConstructor
public class AssessmentRoundController {

    private final AssessmentRoundService assessmentRoundService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssessmentRoundResponse>>> getAllAssessmentRounds(
            @RequestParam(value = "phase_id", required = false) Long phaseId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách đợt đánh giá thành công",
                assessmentRoundService.getAllAssessmentRounds(phaseId)
        ));
    }

    @GetMapping("/{round_id}")
    public ResponseEntity<ApiResponse<AssessmentRoundResponse>> getAssessmentRoundById(
            @PathVariable("round_id") Long roundId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy chi tiết đợt đánh giá thành công",
                assessmentRoundService.getAssessmentRoundById(roundId)
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AssessmentRoundResponse>> createAssessmentRound(
            @Valid @RequestBody AssessmentRoundRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Tạo đợt đánh giá mới thành công",
                assessmentRoundService.createAssessmentRound(request)
        ));
    }

    @PutMapping("/{round_id}")
    public ResponseEntity<ApiResponse<AssessmentRoundResponse>> updateAssessmentRound(
            @PathVariable("round_id") Long roundId,
            @Valid @RequestBody AssessmentRoundRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Cập nhật đợt đánh giá thành công",
                assessmentRoundService.updateAssessmentRound(roundId, request)
        ));
    }

    @DeleteMapping("/{round_id}")
    public ResponseEntity<ApiResponse<Void>> deleteAssessmentRound(
            @PathVariable("round_id") Long roundId) {
        assessmentRoundService.deleteAssessmentRound(roundId);
        return ResponseEntity.ok(ApiResponse.success("Xóa đợt đánh giá thành công"));
    }
}
