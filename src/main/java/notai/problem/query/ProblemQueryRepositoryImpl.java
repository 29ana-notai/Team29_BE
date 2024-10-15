package notai.problem.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import notai.problem.domain.QProblem;
import notai.problem.query.result.ProblemPageContentResult;

import java.util.List;

@RequiredArgsConstructor
public class ProblemQueryRepositoryImpl implements ProblemQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

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

    @Override
    public void deleteProblemByDocumentIdAndPageNumbers(Long documentId, List<Integer> pageNumbers) {
        QProblem problem = QProblem.problem;

        queryFactory.delete(problem)
                    .where(problem.document.id.eq(documentId).and(problem.pageNumber.in(pageNumbers)))
                    .execute();

        entityManager.flush();
        entityManager.clear();
    }
}
