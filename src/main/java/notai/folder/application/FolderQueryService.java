package notai.folder.application;

import lombok.RequiredArgsConstructor;
import notai.folder.application.result.FolderResponse;
import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FolderQueryService {

    private final FolderRepository folderRepository;

    private static final Long ROOT_ID = -1L;

    public List<FolderResponse> getFolders(Long memberId) {
        var folders = folderRepository.findAllByMemberIdOrderByIdAsc(memberId);

        var subFolderMap = new HashMap<Long, List<Long>>();
        var folderMap = new HashMap<Long, Folder>();
        for (var folder : folders) {
            folderMap.put(folder.getId(), folder);
            var parentFolderId = folder.getParentFolder() == null
                    ? ROOT_ID
                    : folder.getParentFolder().getId();
            var subFolders = subFolderMap.getOrDefault(parentFolderId, new ArrayList<>());
            subFolders.add(folder.getId());
            subFolderMap.put(parentFolderId, subFolders);
        }

        var result = new ArrayList<FolderResponse>();
        recursiveInsertFolder(ROOT_ID, subFolderMap, folderMap, result);

        return result;
    }

    private void recursiveInsertFolder(
            Long parentFolderId,
            Map<Long, List<Long>> subFolderMap,
            Map<Long, Folder> folderMap,
            List<FolderResponse> result
    ) {
        var subFolders = subFolderMap.getOrDefault(parentFolderId, new ArrayList<>());

        for (var subFolderId : subFolders) {
            var folder = folderMap.get(subFolderId);
            var folderResponse = getFolderResponse(folder);
            result.add(folderResponse);

            recursiveInsertFolder(subFolderId, subFolderMap, folderMap, folderResponse.subFolders());
        }
    }

    private FolderResponse getFolderResponse(Folder folder) {
        return FolderResponse.of(folder.getId(), folder.getName());
    }
}
