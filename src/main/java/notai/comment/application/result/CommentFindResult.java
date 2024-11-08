package notai.comment.application.result;

import notai.comment.domain.Comment;
import notai.comment.presentation.response.CommentFindResponse;

import java.time.LocalDateTime;

public record CommentFindResult (
        Long id,
        Long postId,
        Long memberId,
        String contents,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentFindResult of(Comment comment) {
        return new CommentFindResult(
                comment.getId(),
                comment.getPost().getId(),
                comment.getMember().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
