package ra.edu.service;

import ra.edu.dto.response.StudentResponse;

import java.util.List;

public interface StudentService {
    List<ra.edu.dto.response.StudentSummaryResponse> getStudents(String username, String role);
    StudentResponse getStudentById(Long studentId, String username, String role);
    StudentResponse createStudent(ra.edu.dto.request.StudentCreateRequest request);
    StudentResponse updateStudent(Long studentId, ra.edu.dto.request.StudentUpdateRequest request, String username, String role);
}
