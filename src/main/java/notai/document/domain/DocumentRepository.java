package notai.document.domain;

import notai.common.exception.type.NotFoundException;
import notai.document.query.DocumentQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long>, DocumentQueryRepository {
    default Document getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("자료를 찾을 수 없습니다."));
    }
}
