package notai.comment.presentation.response;

import notai.comment.application.result.CommentFindResult;
import notai.comment.presentation.response.CommentFindResponse;

import java.time.LocalDateTime;

public record CommentFindResponse (
        Long id,
        Long postId,
        Long memberId,
        String contents,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentFindResponse from(CommentFindResult commentFindResult) {
        return new CommentFindResponse(
                commentFindResult.id(),
                commentFindResult.postId(),
                commentFindResult.memberId(),
                commentFindResult.contents(),
                commentFindResult.createdAt(),
                commentFindResult.updatedAt()
        );

    }
}
