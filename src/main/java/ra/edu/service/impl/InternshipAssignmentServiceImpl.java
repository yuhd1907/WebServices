package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.dto.request.InternshipAssignmentRequest;
import ra.edu.dto.request.InternshipAssignmentStatusRequest;
import ra.edu.dto.response.InternshipAssignmentResponse;
import ra.edu.entity.*;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.mapper.InternshipAssignmentMapper;
import ra.edu.repository.InternshipAssignmentRepository;
import ra.edu.repository.InternshipPhaseRepository;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.util.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternshipAssignmentServiceImpl implements ra.edu.service.InternshipAssignmentService {

    private final InternshipAssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final InternshipPhaseRepository phaseRepository;

    @Override
    public List<InternshipAssignmentResponse> getAllAssignments() {
        String username = SecurityUtils.getCurrentUsername();
        String role = SecurityUtils.getCurrentUserRole();

        List<InternshipAssignment> assignments = assignmentRepository.findAllByRoleAndUsername(role, username);

        return assignments.stream()
                .map(InternshipAssignmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InternshipAssignmentResponse getAssignmentById(Long assignmentId) {
        String username = SecurityUtils.getCurrentUsername();
        String role = SecurityUtils.getCurrentUserRole();

        InternshipAssignment assignment = assignmentRepository.findByIdAndRoleAndUsername(assignmentId, role, username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phân công với ID: " + assignmentId + " hoặc bạn không có quyền truy cập."));

        return InternshipAssignmentMapper.toResponse(assignment);
    }

    @Override
    @Transactional
    public InternshipAssignmentResponse createAssignment(InternshipAssignmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giáo viên hướng dẫn"));
        InternshipPhase phase = phaseRepository.findById(request.getPhaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập"));

        if (assignmentRepository.existsByStudent_StudentIdAndPhase_PhaseId(student.getStudentId(), phase.getPhaseId())) {
            throw new ConflictException("Sinh viên này đã được phân công trong giai đoạn này.");
        }

        InternshipAssignment assignment = InternshipAssignment.builder()
                .student(student)
                .mentor(mentor)
                .phase(phase)
                .build();

        InternshipAssignment saved = assignmentRepository.save(assignment);
        return InternshipAssignmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InternshipAssignmentResponse updateAssignmentStatus(Long assignmentId, InternshipAssignmentStatusRequest request) {
        InternshipAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phân công với ID: " + assignmentId));

        assignment.setStatus(request.getStatus());
        InternshipAssignment updated = assignmentRepository.save(assignment);
        return InternshipAssignmentMapper.toResponse(updated);
    }
}
