package notai.llm.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import notai.llm.application.LLMService;
import notai.llm.application.command.LLMSubmitCommand;
import notai.llm.application.result.LLMSubmitResult;
import notai.llm.presentation.request.LLMSubmitRequest;
import notai.llm.presentation.response.LLMSubmitResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/llm")
@RequiredArgsConstructor
public class LLMController {

    private final LLMService llmService;

    @PostMapping
    public ResponseEntity<LLMSubmitResponse> submitTask(@RequestBody @Valid LLMSubmitRequest request) {
        LLMSubmitCommand command = request.toCommand();
        LLMSubmitResult result = llmService.submitTask(command);
        return ResponseEntity.accepted().body(LLMSubmitResponse.from(result));
    }
}
