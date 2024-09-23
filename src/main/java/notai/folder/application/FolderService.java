package notai.folder.application;

import lombok.RequiredArgsConstructor;
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
}
