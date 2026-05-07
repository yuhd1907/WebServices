package ra.edu.service;

import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.StudentResponse;
import ra.edu.dto.response.StudentSummaryResponse;

import java.util.List;

public interface StudentService {
    List<StudentSummaryResponse> getStudents(String username, String role);
    StudentResponse getStudentById(Long studentId, String username, String role);
    StudentResponse createStudent(StudentCreateRequest request);
    StudentResponse updateStudent(Long studentId, StudentUpdateRequest request, String username, String role);
}
