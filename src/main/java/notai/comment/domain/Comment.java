package notai.comment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@ToString
public class Comment extends RootEntity<Long> {

    private static final int MAX_CONTENT_LENGTH = 255;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id")
    private Comment parentComment;

    @NotNull
    @Size(max = MAX_CONTENT_LENGTH)
    @Column(length = MAX_CONTENT_LENGTH)
    private String content;

    public Comment(
            Member member, Post post, String content
    ) {
        this(member, post, null, content);
    }

    public Comment(
            Member member, Post post,Comment parentComment, String content
    ) {
        validateContent(content);
        this.member = member;
        this.post = post;
        this.parentComment = parentComment;
        this.content = content;
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용은 비워둘 수 없습니다.");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("댓글 내용은 %d자를 초과할 수 없습니다.", MAX_CONTENT_LENGTH)
            );
        }
    }

    public void patch(String newContent) {
        if (newContent != null) {
            validateContent(newContent);
            this.content = newContent;
        }
    }
}
