package notai.comment.domain;

import notai.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CommentRepository extends JpaRepository<Post, BigInteger> {

}
