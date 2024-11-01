package notai.comment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import notai.comment.presentation.request.CommentSaveRequest;
import notai.common.domain.RootEntity;
import notai.member.domain.Member;
import notai.post.domain.Post;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Comment extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id")
    private Comment parentComment;

    @NotNull
    @Column(length = 255)
    private String content;

    public Comment(
            Member member, Post post, String content
    ) {
        this.member = member;
        this.post = post;
        this.content = content;
    }

    public void patch(CommentSaveRequest commentSaveRequest) {

        if(this.id != commentSaveRequest.id())
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id 가 입력됐습니다.");

        if (commentSaveRequest.contents() != null)
            this.content = commentSaveRequest.contents();
    }
}
