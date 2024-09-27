package notai.folder.application;

import lombok.RequiredArgsConstructor;
import notai.folder.application.result.FolderResult;
import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderQueryService {

    private final FolderRepository folderRepository;

    public List<FolderResult> getFolders(Long memberId, Long parentFolderId) {
        var folders = getFoldersWithMemberAndParent(memberId, parentFolderId);
        // document read
        return folders.stream().map(this::getFolderResult).toList();
    }

    private List<Folder> getFoldersWithMemberAndParent(Long memberId, Long parentFolderId) {
        if (parentFolderId == null) {
            return folderRepository.findAllByMemberIdAndParentFolderIsNull(memberId);
        }
        return folderRepository.findAllByMemberIdAndParentFolderId(memberId, parentFolderId);
    }

    private FolderResult getFolderResult(Folder folder) {
        var parentFolderId = folder.getParentFolder() != null ? folder.getParentFolder().getId() : null;
        return FolderResult.of(folder.getId(), parentFolderId, folder.getName());
    }
}
