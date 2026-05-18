package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.AssignStudentsRequest;
import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.MentorResponse;
import ra.edu.service.MentorService;
import ra.edu.util.SecurityUtils;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    /**
     * GET /api/mentors - Lấy danh sách tất cả giáo viên hướng dẫn
     * Quyền: ADMIN, STUDENT
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<MentorResponse>>> getMentors() {
        List<MentorResponse> mentors = mentorService.getMentors();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách giáo viên hướng dẫn thành công", mentors));
    }

    /**
     * GET /api/mentors/{mentor_id} - Xem chi tiết một giáo viên hướng dẫn
     * Quyền: ADMIN, MENTOR, STUDENT
     */
    @GetMapping("/{mentor_id}")
    public ResponseEntity<ApiResponse<MentorResponse>> getMentorById(
            @PathVariable("mentor_id") Long mentorId) {
        MentorResponse mentor = mentorService.getMentorById(mentorId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin giáo viên hướng dẫn thành công", mentor));
    }

    /**
     * POST /api/mentors - Tạo hồ sơ giáo viên hướng dẫn mới
     * Quyền: ADMIN
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MentorResponse>> createMentor(
            @Valid @RequestBody MentorCreateRequest request) {
        MentorResponse mentor = mentorService.createMentor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo hồ sơ giáo viên hướng dẫn thành công", mentor));
    }

    /**
     * PUT /api/mentors/{mentor_id} - Cập nhật thông tin giáo viên hướng dẫn
     * ADMIN: cập nhật tất cả | MENTOR: chỉ cập nhật của mình
     */
    @PutMapping("/{mentor_id}")
    public ResponseEntity<ApiResponse<MentorResponse>> updateMentor(
            @PathVariable("mentor_id") Long mentorId,
            @RequestBody MentorUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = SecurityUtils.extractUsername(userDetails);
        String role     = SecurityUtils.extractRole(userDetails);
        MentorResponse mentor = mentorService.updateMentor(mentorId, request, username, role);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thông tin giáo viên hướng dẫn thành công", mentor));
    }

    /**
     * POST /api/mentors/{mentor_id}/students - Phân công sinh viên cho giáo viên hướng dẫn
     * Quyền: ADMIN
     */
    @PostMapping("/{mentor_id}/students")
    public ResponseEntity<ApiResponse<Void>> assignStudents(
            @PathVariable("mentor_id") Long mentorId,
            @Valid @RequestBody AssignStudentsRequest request) {
        mentorService.assignStudentsToMentor(mentorId, request);
        return ResponseEntity.ok(ApiResponse.success("Phân công sinh viên cho giáo viên hướng dẫn thành công"));
    }
}
