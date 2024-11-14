package notai.folder.application;

import notai.folder.application.result.FolderFindResult;
import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import notai.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FolderQueryServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @InjectMocks
    private FolderQueryService folderQueryService;

    @Mock
    private Member member;

    @BeforeEach
    void setUp() {
        given(member.getId()).willReturn(1L);
    }

    @Test
    @DisplayName("folderId를 -1로 조회하면 루트폴더가 조회된다.")
    void getRootFolders_success() {
        //given
        Folder folder = getFolder(1L, null, "루트폴더");
        List<Folder> expectedResults = List.of(folder);

        when(folderRepository.findAllByMemberIdAndParentFolderIsNull(any(Long.class))).thenReturn(expectedResults);
        //when
        List<FolderFindResult> folders = folderQueryService.getFolders(member.getId(), -1L);
        //then
        Assertions.assertThat(folders.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("부모폴더의 ID를 통해 요청을 하게 되면 해당 부모폴더와 연결된 폴더가 조회된다.")
    void getFolders_success() {
        //given
        Folder folder1 = getFolder(1L, null, "루트폴더");
        Folder folder2 = getFolder(2L, folder1, "서브폴더");
        Folder folder3 = getFolder(3L, folder1, "서브폴더");
        List<Folder> expectedResults = List.of(folder2, folder3);

        when(folderRepository.findAllByMemberIdAndParentFolderId(any(Long.class), any(Long.class))).thenReturn(
                expectedResults);
        //when
        List<FolderFindResult> folders = folderQueryService.getFolders(member.getId(), 1L);
        //then
        Assertions.assertThat(folders.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("존재하지 않는 부모폴더의 ID로 조회를 요청하면 빈 배열을 반환한다.")
    void getFolders_fail_noExistsParentFolderId() {
        //given
        List<Folder> expectedResults = new ArrayList<>();

        when(folderRepository.findAllByMemberIdAndParentFolderId(any(Long.class), any(Long.class))).thenReturn(
                expectedResults);
        //when
        List<FolderFindResult> folders = folderQueryService.getFolders(member.getId(), 10000L);
        //then
        Assertions.assertThat(folders.size()).isEqualTo(0);
    }

    private Folder getFolder(Long id, Folder parentFolder, String name) {
        Member member = mock(Member.class);
        Folder folder = spy(new Folder(member, name, parentFolder));
        lenient().when(folder.getId()).thenReturn(id);
        return folder;
    }
}
