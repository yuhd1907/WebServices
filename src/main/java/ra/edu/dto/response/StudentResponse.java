package ra.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class StudentResponse {
    private Long studentId;
    private String studentCode;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String major;
    private String className;
    private LocalDate dateOfBirth;
    private String address;
    private Boolean isActive;
}
