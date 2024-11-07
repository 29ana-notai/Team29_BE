package notai.post.application.command;

public record PostSaveCommand(
        String title,
        String content
) {
}
