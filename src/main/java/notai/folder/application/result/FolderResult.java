package notai.folder.application.result;

public record FolderResult(
        Long id,
        Long parentId,
        String name
) {
    public static FolderResult of(Long id, Long parentId, String name) {
        return new FolderResult(id, parentId, name);
    }
}
