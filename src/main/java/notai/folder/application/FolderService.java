package notai.folder.application;

import lombok.RequiredArgsConstructor;
import notai.common.exception.type.BadRequestException;
import notai.folder.application.result.FolderResult;
import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import notai.folder.presentation.request.FolderMoveRequest;
import notai.folder.presentation.request.FolderSaveRequest;
import notai.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final MemberRepository memberRepository;

    public FolderResult saveRootFolder(Long memberId, FolderSaveRequest folderSaveRequest) {
        var member = memberRepository.getById(memberId);
        var folder = new Folder(member, folderSaveRequest.name());
        var savedFolder = folderRepository.save(folder);
        return getFolderResult(savedFolder);
    }

    public FolderResult saveSubFolder(Long memberId, FolderSaveRequest folderSaveRequest) {
        var member = memberRepository.getById(memberId);
        var parentFolder = folderRepository.getById(folderSaveRequest.parentFolderId());
        var folder = new Folder(member, folderSaveRequest.name(), parentFolder);
        var savedFolder = folderRepository.save(folder);
        return getFolderResult(savedFolder);
    }

    public void moveRootFolder(Long memberId, Long id) {
        var folder = folderRepository.getById(id);
        if (!folder.getMember().getId().equals(memberId)) {
            throw new BadRequestException("올바르지 않은 요청입니다.");
        }
        folder.moveRootFolder();
        folderRepository.save(folder);
    }

    public void moveNewParentFolder(Long memberId, Long id, FolderMoveRequest folderMoveRequest) {
        var folder = folderRepository.getById(id);
        var parentFolder = folderRepository.getById(folderMoveRequest.targetFolderId());
        if (!folder.getMember().getId().equals(memberId)) {
            throw new BadRequestException("올바르지 않은 요청입니다.");
        }
        folder.moveNewParentFolder(parentFolder);
        folderRepository.save(folder);
    }

    public void deleteFolder(Long id) {
        if (!folderRepository.existsById(id)) {
            throw new BadRequestException("올바르지 않은 요청입니다.");
        }
        folderRepository.deleteById(id);
    }

    private FolderResult getFolderResult(Folder folder) {
        var parentFolderId = folder.getParentFolder() != null ? folder.getParentFolder().getId() : null;
        return FolderResult.of(folder.getId(), parentFolderId, folder.getName());
    }
}
