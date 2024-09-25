package notai.folder.application;

import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import notai.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class FolderQueryServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @InjectMocks
    private FolderQueryService folderQueryService;

    @Test
    @DisplayName("계층적 구조의 폴더 구조 생성")
    void getFolders_success() {
        //given
        var folder1 = getFolder(1L, null, "루트폴더");
        var folder2 = getFolder(2L, folder1, "서브 폴더");
        var folder3 = getFolder(3L, folder2, "서브 폴더");
        var folder4 = getFolder(4L, folder1, "서브 폴더");
        var folder5 = getFolder(5L, folder2, "서브 폴더");
        var folder6 = getFolder(6L, null, "루트폴더");
        var expectedResults = List.of(folder1, folder2, folder3, folder4, folder5, folder6);

        when(folderRepository.findAllByMemberIdOrderByIdAsc(any(Long.class))).thenReturn(expectedResults);
        //when
        var folders = folderQueryService.getFolders(1L);

        Assertions.assertThat(folders.size()).isEqualTo(2);
        Assertions.assertThat(folders.get(0).subFolders().size()).isEqualTo(2);
        Assertions.assertThat(folders.get(0).subFolders().get(0).subFolders().size()).isEqualTo(2);
        Assertions.assertThat(folders.get(1).subFolders().size()).isEqualTo(0);
    }

    private Folder getFolder(Long id, Folder parentFolder, String name) {
        var member = mock(Member.class);
        var folder = spy(new Folder(member, name, parentFolder));
        when(folder.getId()).thenReturn(id);
        return folder;
    }
}
