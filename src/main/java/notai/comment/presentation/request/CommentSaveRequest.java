package notai.comment.presentation.request;


public record CommentSaveRequest(
        Long id,
        Long postId,
        Long memberId,
        String contents
) {
}
