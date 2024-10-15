package notai.llm.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notai.common.exception.type.InternalServerErrorException;
import notai.common.exception.type.NotFoundException;
import notai.document.domain.DocumentRepository;
import notai.llm.application.command.LLMPageStatusCommand;
import notai.llm.application.result.LLMOverallStatusResult;
import notai.llm.application.result.LLMPageStatusResult;
import notai.llm.application.result.LLMResultsResult;
import notai.llm.application.result.LLMResultsResult.LLMContent;
import notai.llm.application.result.LLMResultsResult.LLMResult;
import notai.llm.domain.TaskStatus;
import notai.llm.query.LLMQueryRepository;
import notai.problem.domain.ProblemRepository;
import notai.problem.query.result.ProblemPageContentResult;
import notai.summary.domain.SummaryRepository;
import notai.summary.query.result.SummaryPageContentResult;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static notai.common.exception.ErrorMessages.*;
import static notai.llm.domain.TaskStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LLMQueryService {

    private final LLMQueryRepository llmQueryRepository;
    private final DocumentRepository documentRepository;
    private final SummaryRepository summaryRepository;
    private final ProblemRepository problemRepository;

    public LLMOverallStatusResult fetchOverallStatus(Long documentId) {
        checkDocumentExists(documentId);
        List<Long> summaryIds = getSummaryIds(documentId);
        List<TaskStatus> taskStatuses = getTaskStatuses(summaryIds);

        int totalPages = summaryIds.size();
        int completedPages = Collections.frequency(taskStatuses, COMPLETED);

        if (totalPages == completedPages) {
            return LLMOverallStatusResult.of(documentId, COMPLETED, totalPages, completedPages);
        }
        return LLMOverallStatusResult.of(documentId, IN_PROGRESS, totalPages, completedPages);
    }

    public LLMPageStatusResult fetchPageStatus(LLMPageStatusCommand command) {
        checkDocumentExists(command.documentId());
        Long summaryId =
                summaryRepository.getSummaryIdByDocumentIdAndPageNumber(command.documentId(), command.pageNumber());

        if (summaryId == null) {
            return LLMPageStatusResult.from(NOT_REQUESTED);
        }
        return LLMPageStatusResult.from(llmQueryRepository.getTaskStatusBySummaryId(summaryId));
    }

    public LLMResultsResult findTaskResult(Long documentId) {
        checkDocumentExists(documentId);
        List<SummaryPageContentResult> summaryResults = getSummaryPageContentResults(documentId);
        List<ProblemPageContentResult> problemResults = getProblemPageContentResults(documentId);
        checkSummaryAndProblemCountsEqual(summaryResults, problemResults);

        List<LLMResult> results = summaryResults.stream().map(summaryResult -> {
            LLMContent content = LLMContent.of(
                    summaryResult.content(),
                    findProblemContentByPageNumber(problemResults, summaryResult.pageNumber())
            );
            return LLMResult.of(summaryResult.pageNumber(), content);
        }).toList();

        return LLMResultsResult.of(documentId, results);
    }

    private void checkDocumentExists(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new NotFoundException(DOCUMENT_NOT_FOUND);
        }
    }

    private static void checkSummaryAndProblemCountsEqual(
            List<SummaryPageContentResult> summaryResults, List<ProblemPageContentResult> problemResults
    ) {
        if (summaryResults.size() != problemResults.size()) {
            log.error("요약 개수와 문제 개수가 일치하지 않습니다. 요약: {} 개, 문제: {} 개", summaryResults.size(), problemResults.size());
            throw new InternalServerErrorException(LLM_TASK_RESULT_ERROR);
        }
    }

    private List<Long> getSummaryIds(Long documentId) {
        List<Long> summaryIds = summaryRepository.getSummaryIdsByDocumentId(documentId);
        if (summaryIds.isEmpty()) {
            throw new NotFoundException(LLM_TASK_LOG_NOT_FOUND);
        }
        return summaryIds;
    }

    private List<TaskStatus> getTaskStatuses(List<Long> summaryIds) {
        return summaryIds.stream().map(llmQueryRepository::getTaskStatusBySummaryId).toList();
    }

    private List<SummaryPageContentResult> getSummaryPageContentResults(Long documentId) {
        List<SummaryPageContentResult> summaryResults = summaryRepository.getPageNumbersAndContentByDocumentId(
                documentId);
        if (summaryResults.isEmpty()) {
            throw new NotFoundException(LLM_TASK_LOG_NOT_FOUND);
        }
        return summaryResults;
    }

    private List<ProblemPageContentResult> getProblemPageContentResults(Long documentId) {
        return problemRepository.getPageNumbersAndContentByDocumentId(documentId);
    }

    private String findProblemContentByPageNumber(List<ProblemPageContentResult> results, int pageNumber) {
        return results.stream()
                      .filter(result -> result.pageNumber() == pageNumber)
                      .findFirst()
                      .map(ProblemPageContentResult::content)
                      .orElseThrow(() -> {
                          log.error("{} 페이지에 대한 문제 생성 결과가 없습니다.", pageNumber);
                          return new InternalServerErrorException(LLM_TASK_RESULT_ERROR);
                      });
    }
}
