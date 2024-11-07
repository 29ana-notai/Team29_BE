package notai.comment.application;

import jakarta.transaction.Transactional;
import notai.comment.application.result.CommentFindResult;
import notai.comment.domain.Comment;
import notai.comment.domain.CommentRepository;
import notai.comment.presentation.request.CommentSaveRequest;
import notai.comment.presentation.request.CommentUpdateRequest;
import notai.comment.presentation.response.CommentFindResponse;
import notai.member.domain.Member;
import notai.member.domain.MemberRepository;
import notai.post.domain.Post;
import notai.post.domain.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;

    public List<CommentFindResponse> comments(Long postId) {
        // 댓글조회
        List<Comment> comments = commentRepository.findByPostId(postId);
        // 엔티티 -> dto 변환
        List<CommentFindResponse> commentFindResponses = new ArrayList<CommentFindResponse>();
        for (int i =0 ; i <comments.size() ;i++){
            Comment comment = comments.get(i);
            CommentFindResponse commentFindResponse = CommentFindResponse.from(CommentFindResult.of(comment));
            commentFindResponses.add(commentFindResponse);
        }
        // 결과반환
        return commentFindResponses;
    }

    @Transactional
    public void create(Long postId, CommentSaveRequest commentSaveRequest) {
        Post post =postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패 !" + "대상 게시글이 없습니다."));
        Member member = memberRepository.getById(commentSaveRequest.memberId());
        String content = commentSaveRequest.contents();
        Comment comment = new Comment(member,post,content);
        commentRepository.save(comment);
    }
    @Transactional
    public void update(Long id, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(id)
                                           .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패! 대상 댓글이 없습니다."));
        comment.patch(commentUpdateRequest);  // 필요한 값만 전달
        commentRepository.save(comment);
    }


    @Transactional
    public void delete(Long id) {
    Comment target=commentRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("댓글 삭제 실패!"+
                    "대상이 없습니다."));
    commentRepository.delete(target);
    }
}

