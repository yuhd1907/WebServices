package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.AssessmentResultRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.AssessmentResultResponse;
import ra.edu.service.AssessmentResultService;

import java.util.List;

@RestController
@RequestMapping("/api/assessment_results")
@RequiredArgsConstructor
public class AssessmentResultController {

    private final AssessmentResultService resultService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssessmentResultResponse>>> getAllResults() {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách kết quả đánh giá thành công",
                resultService.getAllResults()
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AssessmentResultResponse>> createOrUpdateResult(
            @Valid @RequestBody AssessmentResultRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Tạo kết quả đánh giá mới thành công",
                resultService.createOrUpdateResult(request)
        ));
    }

    @PutMapping("/{result_id}")
    public ResponseEntity<ApiResponse<AssessmentResultResponse>> updateResult(
            @PathVariable("result_id") Long resultId,
            @Valid @RequestBody AssessmentResultRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Cập nhật kết quả đánh giá thành công",
                resultService.updateResult(resultId, request)
        ));
    }
}
