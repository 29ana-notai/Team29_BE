package notai.problem.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import notai.problem.domain.QProblem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProblemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Long> getProblemIdsByDocumentId(Long documentId) {
        QProblem problem = QProblem.problem;

        return queryFactory
                .select(problem.id)
                .from(problem)
                .where(problem.document.id.eq(documentId))
                .fetch();
    }
}
