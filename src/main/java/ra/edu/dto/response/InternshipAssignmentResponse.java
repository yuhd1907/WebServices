package ra.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ra.edu.entity.AssignmentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InternshipAssignmentResponse {
    private Long assignmentId;
    private Long studentId;
    private String studentName;
    private Long mentorId;
    private String mentorName;
    private Long phaseId;
    private String phaseName;
    private LocalDateTime assignedDate;
    private AssignmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
