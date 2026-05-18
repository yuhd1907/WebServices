package ra.edu.service;

import ra.edu.dto.request.InternshipPhaseRequest;
import ra.edu.dto.response.InternshipPhaseResponse;

import java.util.List;

public interface InternshipPhaseService {
    List<InternshipPhaseResponse> getAllPhases();
    InternshipPhaseResponse getPhaseById(Long phaseId);
    InternshipPhaseResponse createPhase(InternshipPhaseRequest request);
    InternshipPhaseResponse updatePhase(Long phaseId, InternshipPhaseRequest request);
    void deletePhase(Long phaseId);
}
