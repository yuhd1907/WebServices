package ra.edu.service;

import ra.edu.dto.request.AssignStudentsRequest;
import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.MentorResponse;

import java.util.List;

public interface MentorService {
    List<MentorResponse> getMentors();
    MentorResponse getMentorById(Long mentorId);
    MentorResponse createMentor(MentorCreateRequest request);
    MentorResponse updateMentor(Long mentorId, MentorUpdateRequest request, String username, String role);
}
