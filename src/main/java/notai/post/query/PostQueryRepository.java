package notai.post.query;

import notai.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostQueryRepository extends JpaRepository<Post,Long> {
}
