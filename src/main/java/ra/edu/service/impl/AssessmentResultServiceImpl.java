package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.dto.request.AssessmentResultRequest;
import ra.edu.dto.response.AssessmentResultResponse;
import ra.edu.entity.*;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.mapper.AssessmentResultMapper;
import ra.edu.repository.*;
import ra.edu.util.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentResultServiceImpl implements ra.edu.service.AssessmentResultService {

    private final AssessmentResultRepository resultRepository;
    private final InternshipAssignmentRepository assignmentRepository;
    private final AssessmentRoundRepository roundRepository;
    private final EvaluationCriterionRepository criterionRepository;
    private final UserRepository userRepository;

    @Override
    public List<AssessmentResultResponse> getAllResults() {
        String username = SecurityUtils.getCurrentUsername();
        String role = SecurityUtils.getCurrentUserRole();

        List<AssessmentResult> results = resultRepository.findAllByRoleAndUsernameAndAssignmentId(role, username, null);

        return results.stream()
                .map(AssessmentResultMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssessmentResultResponse createOrUpdateResult(AssessmentResultRequest request) {
        String username = SecurityUtils.getCurrentUsername();
        User mentorUser = userRepository.loadUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin tài khoản của bạn"));

        InternshipAssignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phân công thực tập"));

        // Xác thực MENTOR đánh giá phải là người được phân công
        if (!assignment.getMentor().getUser().getUsername().equals(username)) {
            throw new BadRequestException("Bạn không phải là giáo viên hướng dẫn của sinh viên này");
        }

        AssessmentRound round = roundRepository.findById(request.getRoundId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt đánh giá"));

        EvaluationCriterion criterion = criterionRepository.findById(request.getCriterionId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá"));

        // Kiểm tra xem tiêu chí đã được đánh giá cho sinh viên trong đợt này chưa
        if (resultRepository.existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(
                assignment.getAssignmentId(), round.getRoundId(), criterion.getCriterionId())) {
            throw new ConflictException("Tiêu chí này đã được đánh giá trong đợt này rồi. Vui lòng dùng API cập nhật.");
        }

        AssessmentResult result = AssessmentResult.builder()
                .assignment(assignment)
                .round(round)
                .criterion(criterion)
                .score(request.getScore())
                .comments(request.getComments())
                .evaluatedBy(mentorUser)
                .build();

        AssessmentResult saved = resultRepository.save(result);
        return AssessmentResultMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public AssessmentResultResponse updateResult(Long resultId, AssessmentResultRequest request) {
        String username = SecurityUtils.getCurrentUsername();

        AssessmentResult result = resultRepository.findByResultIdAndEvaluatedBy_Username(resultId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kết quả đánh giá hoặc bạn không có quyền cập nhật"));

        result.setScore(request.getScore());
        result.setComments(request.getComments());
        
        AssessmentResult updated = resultRepository.save(result);
        return AssessmentResultMapper.toResponse(updated);
    }
}
