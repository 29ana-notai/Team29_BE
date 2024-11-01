package notai.comment.presentation.request;

import notai.post.application.command.PostSaveCommand;

import java.time.LocalDateTime;

public record CommentSaveRequest(
        Long id,
        Long postId,
        Long memberId,
        String contents,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
