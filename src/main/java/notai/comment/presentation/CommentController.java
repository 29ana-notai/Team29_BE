package notai.comment.presentation;

import notai.comment.application.CommentService;
import notai.comment.presentation.request.CommentSaveRequest;
import notai.comment.presentation.response.CommentFindResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/post/{postId}/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    //댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentFindResponse>> comments(@PathVariable Long postId){

        List<CommentFindResponse> commentFindResponses = commentService.comments(postId);
        return ResponseEntity.status(HttpStatus.OK).body(commentFindResponses);

    }

    //댓글 생성
    @PostMapping
    public ResponseEntity<Void> create (@PathVariable Long postId,@RequestBody CommentSaveRequest commentSaveRequest) {

        commentService.create(postId,commentSaveRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 댓글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody String newContent) {
        commentService.update(id, newContent);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
