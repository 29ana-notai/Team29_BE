package notai.comment.domain;

import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.validation.constraints.NotNull;
import static lombok.AccessLevel.PROTECTED;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notai.common.domain.RootEntity;
import notai.member.domain.Member;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Comment extends RootEntity<Long> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "member_id")
    private Member member;
    @NotNull
    @Column
    private Long postId;
    @NotNull
    @Column
    private Long parentCommentId;
    @NotNull
    @Column(length = 255)
    private String content;

    public Comment(
            Member member, Long postId, String content
    ) {
        this.member = member;
        this.postId = postId;
        this.content = content;
    }

}
