package notai.post.presentation.response;

import notai.post.application.result.PostFindResult;

import java.time.LocalDateTime;

public record PostFindResponse(
        Long id,
        String title,
        String content
) {
    public static PostFindResponse from(PostFindResult postFindResult) {
        return new PostFindResponse(
                postFindResult.id(),
                postFindResult.title(),
                postFindResult.content()
        );
    }
}
