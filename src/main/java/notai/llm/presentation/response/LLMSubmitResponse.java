package notai.llm.presentation.response;

import java.time.LocalDateTime;

public record LLMSubmitResponse(
        Long documentId,
        LocalDateTime createdAt
) {
    public static LLMSubmitResponse of(Long documentId, LocalDateTime createdAt) {
        return new LLMSubmitResponse(documentId, createdAt);
    }
}
