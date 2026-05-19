package ra.edu.mapper;

import ra.edu.dto.response.InternshipAssignmentResponse;
import ra.edu.entity.InternshipAssignment;

public class InternshipAssignmentMapper {

    private InternshipAssignmentMapper() {
        // Private constructor để ẩn constructor mặc định
    }

    public static InternshipAssignmentResponse toResponse(InternshipAssignment assignment) {
        if (assignment == null) {
            return null;
        }
        
        return InternshipAssignmentResponse.builder()
                .assignmentId(assignment.getAssignmentId())
                .studentId(assignment.getStudent().getStudentId())
                .studentName(assignment.getStudent().getUser().getFullName())
                .mentorId(assignment.getMentor().getMentorId())
                .mentorName(assignment.getMentor().getUser().getFullName())
                .phaseId(assignment.getPhase().getPhaseId())
                .phaseName(assignment.getPhase().getPhaseName())
                .assignedDate(assignment.getAssignedDate())
                .status(assignment.getStatus())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }
}
