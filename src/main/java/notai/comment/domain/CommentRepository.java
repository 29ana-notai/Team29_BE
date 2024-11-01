package notai.comment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value="SELECT * FROM comment WHERE post_id = :postId", nativeQuery = true)
    List<Comment> findByPostId(Long postId);
    @Query(value="SELECT * FROM comment WHERE member_id = :memberId", nativeQuery = true)
    List<Comment> findByMemberId(Long memberId);

}
