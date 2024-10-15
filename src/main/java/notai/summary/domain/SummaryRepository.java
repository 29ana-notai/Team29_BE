package notai.summary.domain;

import notai.common.exception.type.NotFoundException;
import notai.summary.query.SummaryQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryRepository extends JpaRepository<Summary, Long>, SummaryQueryRepository {
    default Summary getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("해당 요약 정보를 찾을 수 없습니다."));
    }
}
