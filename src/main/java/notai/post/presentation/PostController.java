package notai.post.presentation;

import lombok.RequiredArgsConstructor;
import notai.auth.Auth;
import notai.post.application.command.PostSaveCommand;
import notai.post.application.PostService;
import notai.post.application.result.PostFindResult;
import notai.post.application.result.PostSaveResult;
import notai.post.presentation.request.PostSaveRequest;
import notai.post.presentation.response.PostFindResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> savePost(
            @Auth Long memberId, @RequestBody PostSaveRequest postSaveRequest
    ) {
        PostSaveCommand postSaveCommand = postSaveRequest.toCommand();
        PostSaveResult postSaveResult = postService.savePost(memberId, postSaveCommand);
        String url = String.format("/api/post/%s", postSaveResult.id());
        return ResponseEntity.created(URI.create(url)).build();
    }

    //post id로 게시글 1개 조회
    @GetMapping(value = "/{postId}")
    public ResponseEntity<PostFindResponse> getPost(
            @PathVariable Long postId
    ) {
        PostFindResult postFindResult = postService.findPost(postId);
        PostFindResponse response = PostFindResponse.from(postFindResult);
        return ResponseEntity.ok(response);
    }

    //게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<PostFindResponse>> getPosts() {
        List<PostFindResult> postFindResults = postService.findPostAll();
        List<PostFindResponse> postFindResponses = postFindResults.stream()
                                                                  .map(PostFindResponse::from)
                                                                  .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(postFindResponses);
    }

    // memberID 에 해당하는 게시글 전체 조회
    @GetMapping(value = "/{memberId}")
    public ResponseEntity<List<PostFindResponse>> getPostByMemberId(
            @Auth Long memberId
    ) {
        List<PostFindResult> postFindResults  = postService.findPostsByMemberId(memberId);
        List<PostFindResponse> postFindResponses = postFindResults.stream()
                                                                  .map(PostFindResponse::from)
                                                                  .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(postFindResponses);
    }

}
