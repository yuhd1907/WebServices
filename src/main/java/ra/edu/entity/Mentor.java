package ra.edu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "mentors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mentor {

    @Id
    @Column(name = "mentor_id")
    private Long mentorId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "mentor_id")
    private User user;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "academic_rank", length = 50)
    private String academicRank;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mentor_students",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> assignedStudents;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
