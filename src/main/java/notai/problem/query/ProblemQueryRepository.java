package notai.problem.query;

import notai.problem.query.result.ProblemPageContentResult;

import java.util.List;

public interface ProblemQueryRepository {

    List<ProblemPageContentResult> getPageNumbersAndContentByDocumentId(Long documentId);

    void deleteProblemByDocumentIdAndPageNumbers(Long documentId, List<Integer> pageNumbers);
}
