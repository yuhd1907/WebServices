package ra.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MentorResponse {
    private Long mentorId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String department;
    private String academicRank;
    private Boolean isActive;
}
