package notai.problem.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import notai.problem.domain.QProblem;
import notai.problem.query.result.ProblemPageContentResult;

import java.util.List;

@RequiredArgsConstructor
public class ProblemQueryRepositoryImpl implements ProblemQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProblemPageContentResult> getPageNumbersAndContentByDocumentId(Long documentId) {
        QProblem problem = QProblem.problem;

        return queryFactory
                .select(Projections.constructor(
                        ProblemPageContentResult.class,
                        problem.pageNumber,
                        problem.content
                ))
                .from(problem)
                .where(problem.document.id.eq(documentId).and(problem.content.isNotNull()))
                .fetch();
    }
}
