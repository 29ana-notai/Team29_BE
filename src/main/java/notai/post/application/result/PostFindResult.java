package notai.post.application.result;

import notai.post.domain.Post;

public record PostFindResult(
        Long id,
        String title,
        String content

) {
    public static PostFindResult of(
            Post post
    ) {
        return new PostFindResult(
                post.getId(),
                post.getTitle(),
                post.getContent()
        );
    }
}
