package notai.document.application;

import notai.document.application.result.DocumentSaveResult;
import notai.document.domain.Document;
import notai.document.domain.DocumentRepository;
import notai.document.presentation.request.DocumentSaveRequest;
import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import notai.member.domain.Member;
import notai.member.domain.MemberRepository;
import notai.ocr.application.OCRService;
import notai.pdf.PdfService;
import notai.pdf.result.PdfSaveResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private FolderRepository folderRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private PdfService pdfService;
    @Mock
    private OCRService ocrService;
    @InjectMocks
    private DocumentService documentService;

    /**
     * public DocumentSaveResult saveDocument(
     * Long memberId, Long folderId, MultipartFile pdfFile, DocumentSaveRequest documentSaveRequest
     * ) {
     * PdfSaveResult pdfSaveResult = pdfService.savePdf(pdfFile);
     * Document document = saveAndReturnDocument(memberId, folderId, documentSaveRequest, pdfSaveResult);
     * ocrService.saveOCR(document, pdfSaveResult.pdf());
     * return DocumentSaveResult.of(document.getId(), document.getName(), document.getUrl());
     * }
     */

    /**
     * Folder folder = folderRepository.getById(folderId);
     * Member member = memberRepository.getById(memberId);
     * Document document = new Document(folder,
     * member,
     * documentSaveRequest.name(),
     * pdfSaveResult.pdfUrl(),
     * pdfSaveResult.totalPages()
     * );
     * return documentRepository.save(document);
     */

    @Test
    @DisplayName("문서를 업로드하게 되면 PDF 를 저장하고, PDF 의 URL 을 결과에 함께 반환한다.")
    void saveDocument_success() {
        PdfSaveResult pdfSaveResult = mock(PdfSaveResult.class);
        when(pdfSaveResult.pdfUrl()).thenReturn("PDF URL");
        when(pdfSaveResult.totalPages()).thenReturn(10);
        when(pdfService.savePdf(any())).thenReturn(pdfSaveResult);

        DocumentSaveRequest documentSaveRequest = new DocumentSaveRequest("문서이름");

        Member member = mock(Member.class);
        when(memberRepository.getById(anyLong())).thenReturn(member);

        Folder folder = getFolder(1L, member);
        when(folderRepository.getById(anyLong())).thenReturn(folder);

        Document document = mock(Document.class);
        when(document.getId()).thenReturn(1L);
        when(document.getName()).thenReturn("문서");
        when(document.getUrl()).thenReturn("PDF URL");
        when(documentRepository.save(any(Document.class))).thenReturn(document);

        doNothing().when(ocrService).saveOCR(any(), any());

        MultipartFile multipartFile = mock(MultipartFile.class);
        //when
        DocumentSaveResult documentSaveResult = documentService.saveDocument(1L,
                1L,
                multipartFile,
                documentSaveRequest
        );
        //then
        Assertions.assertThat(documentSaveResult.id()).isEqualTo(1L);
        Assertions.assertThat(documentSaveResult.url()).isEqualTo("PDF URL");
    }

    private Folder getFolder(Long id, Member member) {
        Folder folder = spy(new Folder(member, ""));
        lenient().when(folder.getId()).thenReturn(id);
        return folder;
    }
}
