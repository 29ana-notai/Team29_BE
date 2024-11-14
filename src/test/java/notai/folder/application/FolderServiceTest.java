package notai.folder.application;

import static notai.common.exception.ErrorMessages.FOLDER_NOT_FOUND;
import notai.common.exception.type.NotFoundException;
import notai.folder.application.result.FolderSaveResult;
import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import notai.folder.presentation.request.FolderSaveRequest;
import notai.member.domain.Member;
import notai.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private FolderService folderService;

    @Test
    @DisplayName("루트폴더를 생성하면 부모폴더가 존재하지 않는 상태로 생성된다.")
    void saveRootFolder_success() {
        Member member = mock(Member.class);
        FolderSaveRequest folderSaveRequest = new FolderSaveRequest(-1L, "루트폴더");
        Folder expectedFolder = getFolder(1L, null, "루트폴더");
        when(memberRepository.getById(anyLong())).thenReturn(member);
        when(folderRepository.save(any(Folder.class))).thenReturn(expectedFolder);
        //when
        FolderSaveResult savedFolderResult = folderService.saveRootFolder(1L, folderSaveRequest);
        //then
        Assertions.assertThat(savedFolderResult.id()).isEqualTo(1L);
        Assertions.assertThat(savedFolderResult.name()).isEqualTo(expectedFolder.getName());
    }

    @Test
    @DisplayName("일반폴더를 생성하게 되면 부모폴더와 연결되어 생성된다.")
    void saveFolder_success() {
        Member member = mock(Member.class);
        FolderSaveRequest folderSaveRequest = new FolderSaveRequest(2L, "서브폴더");
        Folder parentFolder = getFolder(1L, null, "루트폴더");
        Folder expectedFolder = getFolder(2L, parentFolder, "서브폴더");
        when(memberRepository.getById(anyLong())).thenReturn(member);
        when(folderRepository.getById(anyLong())).thenReturn(parentFolder);
        when(folderRepository.save(any(Folder.class))).thenReturn(expectedFolder);
        //when
        FolderSaveResult savedFolderResult = folderService.saveSubFolder(1L, folderSaveRequest);
        //then
        Assertions.assertThat(savedFolderResult.id()).isEqualTo(2L);
        Assertions.assertThat(savedFolderResult.name()).isEqualTo(expectedFolder.getName());
    }

    @Test
    @DisplayName("존재하지 않는 폴더를 부모폴더로 연결지으면 실패한다.")
    void saveFolder_fail_noExistsParentFolder() {
        FolderSaveRequest folderSaveRequest = new FolderSaveRequest(1000L, "서브폴더");
        when(folderRepository.getById(anyLong())).thenThrow(new NotFoundException(FOLDER_NOT_FOUND));
        //when, then
        Assertions.assertThatThrownBy(() -> folderService.saveSubFolder(1L, folderSaveRequest)).isInstanceOf(
                NotFoundException.class);
    }

    private Folder getFolder(Long id, Folder parentFolder, String name) {
        Member member = mock(Member.class);
        Folder folder = spy(new Folder(member, name, parentFolder));
        lenient().when(folder.getId()).thenReturn(id);
        return folder;
    }
}
