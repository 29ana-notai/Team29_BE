package notai.folder.application;

import lombok.RequiredArgsConstructor;
import notai.common.exception.type.BadRequestException;
import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import notai.folder.presentation.request.FolderSaveRequest;
import notai.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final MemberRepository memberRepository;

    public void saveRootFolder(Long memberId, FolderSaveRequest folderSaveRequest) {
        var member = memberRepository.getById(memberId);
        var folder = new Folder(member, folderSaveRequest.name());
        folderRepository.save(folder);
    }

    public void saveSubFolder(Long memberId, Long parentFolderId, FolderSaveRequest folderSaveRequest) {
        var member = memberRepository.getById(memberId);
        var parentFolder = folderRepository.getById(parentFolderId);
        var folder = new Folder(member, folderSaveRequest.name(), parentFolder);
        folderRepository.save(folder);
    }

    public void moveRootFolder(Long memberId, Long id) {
        var folder = folderRepository.getById(id);
        if (!folder.getMember().getId().equals(memberId)) {
            throw new BadRequestException("올바르지 않은 요청입니다.");
        }
        folder.moveRootFolder();
        folderRepository.save(folder);
    }

    public void moveNewParentFolder(Long memberId, Long id, Long parentFolderId) {
        var folder = folderRepository.getById(id);
        var parentFolder = folderRepository.getById(parentFolderId);
        if (!folder.getMember().getId().equals(memberId)) {
            throw new BadRequestException("올바르지 않은 요청입니다.");
        }
        folder.moveNewParentFolder(parentFolder);
        folderRepository.save(folder);
    }

    public void deleteFolder(Long id) {
        var subFolders = folderRepository.findAllByParentFolderId(id);
        for (var folder : subFolders) {
            deleteFolder(folder.getId());
        }
        // deleteDocument
        folderRepository.deleteById(id);
    }
}
