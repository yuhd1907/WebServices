package ra.edu.service;

import ra.edu.dto.request.AssessmentResultRequest;
import ra.edu.dto.response.AssessmentResultResponse;

import java.util.List;

public interface AssessmentResultService {
    List<AssessmentResultResponse> getAllResults();
    AssessmentResultResponse createOrUpdateResult(AssessmentResultRequest request); // Cho API tạo kết quả
    AssessmentResultResponse updateResult(Long resultId, AssessmentResultRequest request); // Cho API cập nhật kết quả
}
