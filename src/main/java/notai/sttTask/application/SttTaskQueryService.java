package notai.sttTask.application;

import lombok.RequiredArgsConstructor;
import notai.document.domain.Document;
import notai.document.domain.DocumentRepository;
import notai.llm.domain.TaskStatus;
import static notai.llm.domain.TaskStatus.*;
import notai.member.domain.Member;
import notai.member.domain.MemberRepository;
import notai.stt.domain.Stt;
import notai.stt.domain.SttRepository;
import notai.sttTask.application.command.SttTaskPageStatusCommand;
import notai.sttTask.application.result.SttTaskOverallStatusResult;
import notai.sttTask.application.result.SttTaskPageStatusResult;
import notai.sttTask.domain.SttTask;
import notai.sttTask.domain.SttTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SttTaskQueryService {

    private final DocumentRepository documentRepository;
    private final MemberRepository memberRepository;
    private final SttRepository sttRepository;
    private final SttTaskRepository sttTaskRepository;

    public SttTaskOverallStatusResult fetchOverallStatus(Long memberId, Long documentId) {
        Document foundDocument = documentRepository.getById(documentId);
        Member member = memberRepository.getById(memberId);
        foundDocument.validateOwner(member);

        List<Stt> sttResults = sttRepository.findAllByDocumentId(documentId);

        if (sttResults.isEmpty()) {
            return SttTaskOverallStatusResult.of(documentId, NOT_REQUESTED, 0, 0);
        }

        List<TaskStatus> taskStatuses = sttResults.stream()
                .map(stt -> stt.getSttTask().getStatus())
                .distinct()
                .toList();

        int totalPages = foundDocument.getTotalPages();
        int completedPages = Collections.frequency(taskStatuses, COMPLETED);

        if (taskStatuses.size() == 1 && taskStatuses.get(0) == COMPLETED) {
            return SttTaskOverallStatusResult.of(documentId, COMPLETED, totalPages, totalPages);
        }
        return SttTaskOverallStatusResult.of(documentId, IN_PROGRESS, totalPages, completedPages);
    }
}
