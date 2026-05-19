package ra.edu.service;

import ra.edu.dto.request.InternshipAssignmentRequest;
import ra.edu.dto.request.InternshipAssignmentStatusRequest;
import ra.edu.dto.response.InternshipAssignmentResponse;

import java.util.List;

public interface InternshipAssignmentService {
    List<InternshipAssignmentResponse> getAllAssignments();
    InternshipAssignmentResponse getAssignmentById(Long assignmentId);
    InternshipAssignmentResponse createAssignment(InternshipAssignmentRequest request);
    InternshipAssignmentResponse updateAssignmentStatus(Long assignmentId, InternshipAssignmentStatusRequest request);
}
