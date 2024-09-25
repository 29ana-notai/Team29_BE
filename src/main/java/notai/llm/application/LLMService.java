package notai.llm.application;

import lombok.RequiredArgsConstructor;
import notai.common.exception.type.NotFoundException;
import notai.document.domain.Document;
import notai.document.domain.DocumentRepository;
import notai.llm.application.command.LLMSubmitCommand;
import notai.llm.application.result.LLMSubmitResult;
import notai.llm.domain.LLM;
import notai.llm.domain.LLMRepository;
import notai.problem.domain.Problem;
import notai.summary.domain.Summary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * SummaryService 와 ExamService 는 엔티티와 관련된 로직만 처리하고
 * AI 요약 및 문제 생성 요청은 여기서 처리하는 식으로 생각했습니다.
 * AI 서버와의 통신은 별도 클래스에서 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class LLMService {

    private final LLMRepository llmRepository;
    private final DocumentRepository documentRepository;

    public LLMSubmitResult submitTask(LLMSubmitCommand command) {
        // TODO: document 개발 코드 올려주시면, getById 로 수정
        Document foundDocument =
                documentRepository.findById(command.documentId()).orElseThrow(() -> new NotFoundException(""));

        command.pages().forEach(pageNumber -> {
            UUID taskId = sendRequestToAIServer();
            Summary summary = new Summary(foundDocument, pageNumber);
            Problem problem = new Problem(foundDocument, pageNumber);

            LLM llm = new LLM(taskId, summary, problem);
            llmRepository.save(llm);
        });

        return LLMSubmitResult.of(command.documentId(), LocalDateTime.now());
    }

    /**
     * 임시 값 반환, 추후 AI 서버에서 작업 단위 UUID 가 반환됨.
     */
    private UUID sendRequestToAIServer() {
        return UUID.randomUUID();
    }
}
