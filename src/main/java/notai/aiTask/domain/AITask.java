package notai.aiTask.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notai.common.domain.RootEntity;
import notai.problem.domain.Problem;
import notai.summary.domain.Summary;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(name = "ai_task")
public class AITask extends RootEntity<UUID> {

    @Id
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "summary_id")
    private Summary summary;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(length = 20)
    private TaskStatus status;

    public AITask(UUID id, Summary summary, Problem problem) {
        this.id = id;
        this.summary = summary;
        this.problem = problem;
        this.status = TaskStatus.PENDING;
    }
}
