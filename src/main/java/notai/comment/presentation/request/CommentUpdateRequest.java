package notai.comment.presentation.request;

public record CommentUpdateRequest(
        Long id,
        Long postId,
        Long memberId,
        String contents
) {
}
