package notai.folder.application.result;

public record FolderResponse(
        Long id,
        String name
) {
    public static FolderResponse of(Long id, String name) {
        return new FolderResponse(id, name);
    }
}
