package notai.folder.application;

import lombok.RequiredArgsConstructor;
import notai.common.exception.type.BadRequestException;
import notai.folder.application.result.FolderMoveResult;
import notai.folder.application.result.FolderSaveResult;
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

    public FolderSaveResult saveRootFolder(Long memberId, FolderSaveRequest folderSaveRequest) {
        var member = memberRepository.getById(memberId);
        var folder = new Folder(member, folderSaveRequest.name());
        var savedFolder = folderRepository.save(folder);
        return getFolderSaveResult(savedFolder);
    }

    public FolderSaveResult saveSubFolder(Long memberId, FolderSaveRequest folderSaveRequest) {
        var member = memberRepository.getById(memberId);
        var parentFolder = folderRepository.getById(folderSaveRequest.parentFolderId());
        var folder = new Folder(member, folderSaveRequest.name(), parentFolder);
        var savedFolder = folderRepository.save(folder);
        return getFolderSaveResult(savedFolder);
    }

    public FolderMoveResult moveRootFolder(Long memberId, Long id) {
        var folder = folderRepository.getById(id);
        if (!folder.getMember().getId().equals(memberId)) {
            throw new BadRequestException("올바르지 않은 요청입니다.");
        }
        folder.moveRootFolder();
        folderRepository.save(folder);
        return getFolderMoveResult(folder);
    }

    public FolderMoveResult moveNewParentFolder(Long memberId, Long id, FolderMoveRequest folderMoveRequest) {
        var folder = folderRepository.getById(id);
        var parentFolder = folderRepository.getById(folderMoveRequest.targetFolderId());
        if (!folder.getMember().getId().equals(memberId)) {
            throw new BadRequestException("올바르지 않은 요청입니다.");
        }
        folder.moveNewParentFolder(parentFolder);
        folderRepository.save(folder);
        return getFolderMoveResult(folder);
    }

    public void deleteFolder(Long memberId, Long id) {
        if (!folderRepository.existsByMemberIdAndId(memberId, id)) {
            throw new BadRequestException("올바르지 않은 요청입니다.");
        }
        folderRepository.deleteById(id);
    }

    private FolderSaveResult getFolderSaveResult(Folder folder) {
        var parentFolderId = folder.getParentFolder() != null ? folder.getParentFolder().getId() : null;
        return FolderSaveResult.of(folder.getId(), parentFolderId, folder.getName());
    }

    private FolderMoveResult getFolderMoveResult(Folder folder) {
        return FolderMoveResult.of(folder.getId(), folder.getName());
    }
}
