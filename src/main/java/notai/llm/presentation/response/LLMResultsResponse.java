package notai.llm.presentation.response;

import java.util.List;

public record LLMResultsResponse(
        Long documentId,
        Integer totalPages,
        List<Result> results
) {
    public static LLMResultsResponse of(Long documentId, List<Result> results) {
        return new LLMResultsResponse(documentId, results.size(), results);
    }

    public record Result(
            Integer pageNumber,
            Content content
    ) {
        public static Result of(Integer pageNumber, Content content) {
            return new Result(pageNumber, content);
        }
    }

    public record Content(
            String summary,
            String problem
    ) {
        public static Content of(String summary, String problem) {
            return new Content(summary, problem);
        }
    }
}
