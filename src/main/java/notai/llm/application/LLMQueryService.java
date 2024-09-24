package notai.llm.application;

import lombok.RequiredArgsConstructor;
import notai.common.exception.type.BadRequestException;
import notai.common.exception.type.NotFoundException;
import notai.document.domain.DocumentRepository;
import notai.llm.application.result.LLMStatusResult;
import notai.llm.domain.TaskStatus;
import static notai.llm.domain.TaskStatus.COMPLETED;
import static notai.llm.domain.TaskStatus.IN_PROGRESS;
import notai.llm.query.LLMQueryRepository;
import notai.summary.query.SummaryQueryRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LLMQueryService {

    private final LLMQueryRepository LLMQueryRepository;
    private final DocumentRepository documentRepository;
    private final SummaryQueryRepository summaryQueryRepository;

    public LLMStatusResult fetchTaskStatus(Long documentId) {
        checkDocumentExists(documentId);
        List<Long> summaryIds = summaryQueryRepository.getSummaryIdsByDocumentId(documentId);
        if (summaryIds.isEmpty()) {
            throw new BadRequestException("AI 기능을 요청한 기록이 없습니다.");
        }
        List<TaskStatus> taskStatuses = summaryIds.stream()
                .map(LLMQueryRepository::getTaskStatusBySummaryId)
                .toList();

        int totalPages = summaryIds.size();
        int completedPages = Collections.frequency(taskStatuses, COMPLETED);

        if (totalPages == completedPages) {
            return LLMStatusResult.of(documentId, COMPLETED, totalPages, completedPages);
        }
        return LLMStatusResult.of(documentId, IN_PROGRESS, totalPages, completedPages);
    }

    private void checkDocumentExists(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new NotFoundException("해당 강의자료를 찾을 수 없습니다.");
        }
    }
}
