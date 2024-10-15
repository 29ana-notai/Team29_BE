package notai.summary.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import notai.summary.domain.QSummary;
import notai.summary.query.result.SummaryPageContentResult;

import java.util.List;

@RequiredArgsConstructor
public class SummaryQueryRepositoryImpl implements SummaryQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public List<Long> getSummaryIdsByDocumentId(Long documentId) {
        QSummary summary = QSummary.summary;

        return queryFactory
                .select(summary.id)
                .from(summary)
                .where(summary.document.id.eq(documentId))
                .fetch();
    }

    @Override
    public List<SummaryPageContentResult> getPageNumbersAndContentByDocumentId(Long documentId) {
        QSummary summary = QSummary.summary;

        return queryFactory
                .select(Projections.constructor(
                        SummaryPageContentResult.class,
                        summary.pageNumber,
                        summary.content
                ))
                .from(summary)
                .where(summary.document.id.eq(documentId).and(summary.content.isNotNull()))
                .fetch();
    }

    @Override
    public void deleteSummaryByDocumentIdAndPageNumbers(Long documentId, List<Integer> pageNumbers) {
        QSummary summary = QSummary.summary;

        queryFactory.delete(summary)
                    .where(summary.document.id.eq(documentId).and(summary.pageNumber.in(pageNumbers)))
                    .execute();

        entityManager.flush();
        entityManager.clear();
    }
}
