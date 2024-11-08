package notai.post.application;

import lombok.RequiredArgsConstructor;
import notai.member.domain.Member;
import notai.member.domain.MemberRepository;
import notai.post.application.command.PostSaveCommand;
import notai.post.application.result.PostFindResult;
import notai.post.application.result.PostSaveResult;
import notai.post.domain.Post;
import notai.post.domain.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostSaveResult savePost(Long memberId, PostSaveCommand postSaveCommand) {
        Member member = memberRepository.getById(memberId);
        Post post = new Post( member, postSaveCommand.title(), postSaveCommand.content());
        Post savedPost = postRepository.save(post);
        return PostSaveResult.of(savedPost.getId(), savedPost.getTitle());
    }

    public PostFindResult findPost(Long postId) {
        Post post = postRepository.findById(postId).get();
        return PostFindResult.of(post);
    }

    public List<PostFindResult> findPostAll() {
        //게시글 조회
        List<Post> posts = postRepository.findAll();
        // 엔티티 -> dto 변환
        List<PostFindResult> postFindResponses = new ArrayList<>();
        for (int i = 0 ; i < posts.size() ; i++){
            Post post =posts.get(i);
            PostFindResult postFindResult = PostFindResult.of(post);
            postFindResponses.add(postFindResult);
        }
        return postFindResponses;
    }

    public List<PostFindResult> findPostsByMemberId(Long memberId) {

        List<Post> posts = postRepository.findAllByMemberId(memberId);
        List<PostFindResult> postFindResponses = new ArrayList<>();
        for (int i = 0 ; i < posts.size() ; i++){
            Post post =posts.get(i);
            PostFindResult postFindResult = PostFindResult.of(post);
            postFindResponses.add(postFindResult);
        }
        return postFindResponses;
    }
}
