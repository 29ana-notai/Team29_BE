package notai.problem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notai.common.domain.RootEntity;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Problem extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private Integer pageNumber;

    @Column(columnDefinition = "TEXT")
    private String content;
}
