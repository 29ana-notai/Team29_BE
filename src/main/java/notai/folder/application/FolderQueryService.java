package notai.folder.application;

import lombok.RequiredArgsConstructor;
import notai.folder.application.result.FolderResponse;
import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderQueryService {

    private final FolderRepository folderRepository;

    public List<FolderResponse> getFolders(Long memberId, Long parentFolderId) {
        var folders = getFoldersWithMemberAndParent(memberId, parentFolderId);
        // document read
        return folders.stream().map(this::getFolderResponse).toList();
    }

    private List<Folder> getFoldersWithMemberAndParent(Long memberId, Long parentFolderId) {
        if (parentFolderId == null) {
            return folderRepository.findAllByMemberIdAndParentFolderIsNull(memberId);
        }
        return folderRepository.findAllByMemberIdAndParentFolderId(memberId, parentFolderId);
    }

    private FolderResponse getFolderResponse(Folder folder) {
        return FolderResponse.of(folder.getId(), folder.getName());
    }
}
