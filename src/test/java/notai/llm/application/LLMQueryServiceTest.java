package notai.llm.application;

import notai.common.exception.type.NotFoundException;
import notai.document.domain.DocumentRepository;
import notai.llm.application.result.LLMStatusResult;
import static notai.llm.domain.TaskStatus.*;
import notai.llm.query.LLMQueryRepository;
import notai.summary.query.SummaryQueryRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class LLMQueryServiceTest {

    @InjectMocks
    private LLMQueryService llmQueryService;

    @Mock
    private LLMQueryRepository llmQueryRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private SummaryQueryRepository summaryQueryRepository;

    @Test
    void 존재하지_않는_문서ID로_요청한_경우_예외_발생() {
        // given
        given(documentRepository.existsById(anyLong())).willReturn(false);

        // when & then
        assertThrows(NotFoundException.class, () -> {
            llmQueryService.fetchTaskStatus(1L);
        });
    }

    @Test
    void 모든_페이지의_작업이_완료된_경우() {
        // given
        Long documentId = 1L;
        List<Long> summaryIds = List.of(1L, 2L, 3L);

        given(documentRepository.existsById(anyLong())).willReturn(true);
        given(summaryQueryRepository.getSummaryIdsByDocumentId(documentId)).willReturn(summaryIds);
        given(llmQueryRepository.getTaskStatusBySummaryId(1L)).willReturn(COMPLETED);
        given(llmQueryRepository.getTaskStatusBySummaryId(2L)).willReturn(COMPLETED);
        given(llmQueryRepository.getTaskStatusBySummaryId(3L)).willReturn(COMPLETED);

        // when
        LLMStatusResult result = llmQueryService.fetchTaskStatus(documentId);

        // then
        assertThat(result.overallStatus()).isEqualTo(COMPLETED);
        assertThat(result.totalPages()).isEqualTo(3);
        assertThat(result.completedPages()).isEqualTo(3);
    }

    @Test
    void 작업이_진행중인_경우() {
        // given
        Long documentId = 1L;
        List<Long> summaryIds = List.of(1L, 2L, 3L);

        given(documentRepository.existsById(anyLong())).willReturn(true);
        given(summaryQueryRepository.getSummaryIdsByDocumentId(documentId)).willReturn(summaryIds);
        given(llmQueryRepository.getTaskStatusBySummaryId(1L)).willReturn(COMPLETED);
        given(llmQueryRepository.getTaskStatusBySummaryId(2L)).willReturn(IN_PROGRESS);
        given(llmQueryRepository.getTaskStatusBySummaryId(3L)).willReturn(PENDING);

        // when
        LLMStatusResult result = llmQueryService.fetchTaskStatus(documentId);

        // then
        assertThat(result.overallStatus()).isEqualTo(IN_PROGRESS);
        assertThat(result.totalPages()).isEqualTo(3);
        assertThat(result.completedPages()).isEqualTo(1);
    }
}