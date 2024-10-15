package notai.llm.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import notai.llm.application.LLMQueryService;
import notai.llm.application.LLMService;
import notai.llm.application.command.LLMPageStatusCommand;
import notai.llm.application.command.LLMSubmitCommand;
import notai.llm.application.command.SummaryAndProblemUpdateCommand;
import notai.llm.application.result.LLMOverallStatusResult;
import notai.llm.application.result.LLMPageStatusResult;
import notai.llm.application.result.LLMResultsResult;
import notai.llm.application.result.LLMSubmitResult;
import notai.llm.presentation.request.LLMSubmitRequest;
import notai.llm.presentation.request.SummaryAndProblemUpdateRequest;
import notai.llm.presentation.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/llm")
@RequiredArgsConstructor
public class LLMController {

    private final LLMService llmService;
    private final LLMQueryService llmQueryService;

    @PostMapping
    public ResponseEntity<LLMSubmitResponse> submitTask(@RequestBody @Valid LLMSubmitRequest request) {
        LLMSubmitCommand command = request.toCommand();
        LLMSubmitResult result = llmService.submitTasks(command);
        return ResponseEntity.accepted().body(LLMSubmitResponse.from(result));
    }

    @GetMapping("/status/{documentId}")
    public ResponseEntity<LLMOverallStatusResponse> fetchOverallStatus(@PathVariable("documentId") Long documentId) {
        LLMOverallStatusResult result = llmQueryService.fetchOverallStatus(documentId);
        return ResponseEntity.ok(LLMOverallStatusResponse.from(result));
    }

    @GetMapping("/status/{documentId}/{pageNumber}")
    public ResponseEntity<LLMPageStatusResponse> fetchPageStatus(
            @PathVariable("documentId") Long documentId, @PathVariable("pageNumber") Integer pageNumber
    ) {
        LLMPageStatusCommand command = LLMPageStatusCommand.of(documentId, pageNumber);
        LLMPageStatusResult result = llmQueryService.fetchPageStatus(command);
        return ResponseEntity.ok(LLMPageStatusResponse.from(result));
    }

    @GetMapping("/results/{documentId}")
    public ResponseEntity<LLMResultsResponse> findTaskResult(@PathVariable("documentId") Long documentId) {
        LLMResultsResult result = llmQueryService.findTaskResult(documentId);
        return ResponseEntity.ok(LLMResultsResponse.from(result));
    }

    @PostMapping("/callback")
    public ResponseEntity<SummaryAndProblemUpdateResponse> handleTaskCallback(
            @RequestBody @Valid SummaryAndProblemUpdateRequest request
    ) {
        SummaryAndProblemUpdateCommand command = request.toCommand();
        Integer receivedPage = llmService.updateSummaryAndProblem(command);
        return ResponseEntity.ok(SummaryAndProblemUpdateResponse.from(receivedPage));
    }
}
