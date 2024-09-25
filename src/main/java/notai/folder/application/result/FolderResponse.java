package notai.folder.application.result;

import java.util.ArrayList;
import java.util.List;

public record FolderResponse(
        Long id,
        String name,
        List<FolderResponse> subFolders
) {
    public static FolderResponse of(Long id, String name) {
        return new FolderResponse(id, name, new ArrayList<>());
    }
}
