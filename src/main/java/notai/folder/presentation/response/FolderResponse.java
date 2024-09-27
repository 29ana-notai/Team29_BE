package notai.folder.presentation.response;

import notai.folder.application.result.FolderResult;

public record FolderResponse(
        Long id,
        Long parentId,
        String name
) {
    public static FolderResponse from(FolderResult folderResult) {
        return new FolderResponse(folderResult.id(), folderResult.parentId(), folderResult.name());
    }
}
