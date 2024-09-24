package notai.summary.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import notai.summary.domain.QSummary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SummaryQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Long> getSummaryIdsByDocumentId(Long documentId) {
        QSummary summary = QSummary.summary;

        return queryFactory
                .select(summary.id)
                .from(summary)
                .where(summary.document.id.eq(documentId))
                .fetch();
    }
}
