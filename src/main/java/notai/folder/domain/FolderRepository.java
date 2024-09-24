package notai.folder.domain;

import notai.common.exception.type.NotFoundException;
import notai.folder.query.FolderQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long>, FolderQueryRepository {
    default Folder getById(Long id) {
        return findById(id).orElseThrow(() ->
                new NotFoundException("폴더 정보를 찾을 수 없습니다.")
        );
    }

    List<Folder> findAllByMemberIdOrderByIdAsc(Long memberId);
}
