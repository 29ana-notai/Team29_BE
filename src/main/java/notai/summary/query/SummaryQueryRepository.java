package notai.summary.query;

import notai.summary.query.result.SummaryPageContentResult;

import java.util.List;

public interface SummaryQueryRepository {

    List<Long> getSummaryIdsByDocumentId(Long documentId);

    List<SummaryPageContentResult> getPageNumbersAndContentByDocumentId(Long documentId);

    Long getSummaryIdByDocumentIdAndPageNumber(Long documentId, int pageNumber);
}
