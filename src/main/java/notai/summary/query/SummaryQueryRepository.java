package notai.summary.query;

import notai.summary.query.result.SummaryPageContentResult;

import java.util.List;

public interface SummaryQueryRepository {

    List<Long> getSummaryIdsByDocumentId(Long documentId);

    List<SummaryPageContentResult> getPageNumbersAndContentByDocumentId(Long documentId);

    void deleteSummaryByDocumentIdAndPageNumbers(Long documentId, List<Integer> pageNumbers);
}
