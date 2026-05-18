package ra.edu.mapper;

import ra.edu.dto.response.MentorResponse;
import ra.edu.entity.Mentor;

/**
 * Mapper chuyển đổi giữa Mentor entity và các DTO liên quan
 */
public class MentorMapper {

    private MentorMapper() {}

    public static MentorResponse toResponse(Mentor mentor) {
        return MentorResponse.builder()
                .mentorId(mentor.getMentorId())
                .fullName(mentor.getUser().getFullName())
                .email(mentor.getUser().getEmail())
                .phoneNumber(mentor.getUser().getPhoneNumber())
                .department(mentor.getDepartment())
                .academicRank(mentor.getAcademicRank())
                .isActive(mentor.getUser().getIsActive())
                .build();
    }
}
