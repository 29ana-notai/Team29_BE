package notai.problem.domain;

import notai.common.exception.type.NotFoundException;
import notai.problem.query.ProblemQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import static notai.common.exception.ErrorMessages.PROBLEM_NOT_FOUND;

public interface ProblemRepository extends JpaRepository<Problem, Long>, ProblemQueryRepository {
    default Problem getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(PROBLEM_NOT_FOUND));
    }
}
