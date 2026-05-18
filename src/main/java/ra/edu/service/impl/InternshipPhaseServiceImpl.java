package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.edu.dto.request.InternshipPhaseRequest;
import ra.edu.dto.response.InternshipPhaseResponse;
import ra.edu.entity.InternshipPhase;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.repository.InternshipPhaseRepository;
import ra.edu.service.InternshipPhaseService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements InternshipPhaseService {

    private final InternshipPhaseRepository phaseRepository;

    @Override
    public List<InternshipPhaseResponse> getAllPhases() {
        return phaseRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InternshipPhaseResponse getPhaseById(Long phaseId) {
        InternshipPhase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập với id: " + phaseId));
        return toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse createPhase(InternshipPhaseRequest request) {
        if (phaseRepository.existsByPhaseName(request.getPhaseName())) {
            throw new ConflictException("Tên giai đoạn thực tập đã tồn tại: " + request.getPhaseName());
        }
        InternshipPhase phase = InternshipPhase.builder()
                .phaseName(request.getPhaseName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .build();
        return toResponse(phaseRepository.save(phase));
    }

    @Override
    public InternshipPhaseResponse updatePhase(Long phaseId, InternshipPhaseRequest request) {
        InternshipPhase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập với id: " + phaseId));

        // Kiểm tra trùng tên (nếu đổi sang tên khác)
        if (!phase.getPhaseName().equals(request.getPhaseName())
                && phaseRepository.existsByPhaseName(request.getPhaseName())) {
            throw new ConflictException("Tên giai đoạn thực tập đã tồn tại: " + request.getPhaseName());
        }

        phase.setPhaseName(request.getPhaseName());
        phase.setStartDate(request.getStartDate());
        phase.setEndDate(request.getEndDate());
        phase.setDescription(request.getDescription());

        return toResponse(phaseRepository.save(phase));
    }

    @Override
    public void deletePhase(Long phaseId) {
        if (!phaseRepository.existsById(phaseId)) {
            throw new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập với id: " + phaseId);
        }
        phaseRepository.deleteById(phaseId);
    }

    private InternshipPhaseResponse toResponse(InternshipPhase phase) {
        return InternshipPhaseResponse.builder()
                .phaseId(phase.getPhaseId())
                .phaseName(phase.getPhaseName())
                .startDate(phase.getStartDate())
                .endDate(phase.getEndDate())
                .description(phase.getDescription())
                .createdAt(phase.getCreatedAt())
                .updatedAt(phase.getUpdatedAt())
                .build();
    }
}
