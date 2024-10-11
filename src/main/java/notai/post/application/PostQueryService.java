package notai.post.application;

import lombok.RequiredArgsConstructor;
import notai.post.application.result.PostFindResult;
import notai.post.domain.Post;
import notai.post.domain.PostRepository;
import notai.post.presentation.response.PostFindResponse;
import notai.post.query.PostQueryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryService {
    private final PostQueryRepository postQueryRepository;
    public PostFindResult findPost(Long postId) {
        Post post = postQueryRepository.findById(postId).get();
        return PostFindResult.of(post);
    }
}
