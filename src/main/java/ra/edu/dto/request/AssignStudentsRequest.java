package ra.edu.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssignStudentsRequest {
    @NotEmpty(message = "Danh sách ID sinh viên không được để trống")
    private List<Long> studentIds;
}
