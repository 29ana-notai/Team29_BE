package notai.llm.application;

import notai.common.exception.type.NotFoundException;
import notai.document.domain.Document;
import notai.document.domain.DocumentRepository;
import notai.llm.application.command.LLMSubmitCommand;
import notai.llm.application.result.LLMSubmitResult;
import notai.llm.domain.LLM;
import notai.llm.domain.LLMRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LLMServiceTest {

    @InjectMocks
    private LLMService llmService;

    @Mock
    private LLMRepository llmRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Test
    void AI_기능_요청시_존재하지_않는_문서ID로_요청한_경우_예외_발생() {
        // given
        Long documentId = 1L;
        List<Integer> pages = List.of(1, 2, 3);
        LLMSubmitCommand command = new LLMSubmitCommand(documentId, pages);

        given(documentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertAll(
                () -> assertThrows(NotFoundException.class, () -> llmService.submitTask(command)),
                () -> verify(documentRepository, times(1)).findById(documentId),
                () -> verify(llmRepository, never()).save(any(LLM.class))
        );
    }

    @Test
    void AI_기능_요청() {
        // given
        Long documentId = 1L;
        List<Integer> pages = List.of(1, 2, 3);
        LLMSubmitCommand command = new LLMSubmitCommand(documentId, pages);
        Document document = mock(Document.class);

        given(documentRepository.findById(anyLong())).willReturn(Optional.of(document));
        given(llmRepository.save(any(LLM.class))).willAnswer(InvocationOnMock::getArguments);

        // when
        LLMSubmitResult result = llmService.submitTask(command);

        // then
        assertAll(
                () -> verify(documentRepository, times(1)).findById(anyLong()),
                () -> verify(llmRepository, times(3)).save(any(LLM.class))
        );
    }
}