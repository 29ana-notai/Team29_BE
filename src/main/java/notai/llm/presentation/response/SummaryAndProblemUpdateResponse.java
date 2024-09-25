package notai.llm.presentation.response;

public record SummaryAndProblemUpdateResponse(
        Integer receivedPage
) {
    public static SummaryAndProblemUpdateResponse of(Integer receivedPage) {
        return new SummaryAndProblemUpdateResponse(receivedPage);
    }
}
