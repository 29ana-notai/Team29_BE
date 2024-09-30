package notai.document.application;

import lombok.RequiredArgsConstructor;
import notai.document.application.result.DocumentSaveResult;
import notai.document.domain.Document;
import notai.document.domain.DocumentRepository;
import notai.document.presentation.request.DocumentSaveRequest;
import notai.folder.domain.Folder;
import notai.folder.domain.FolderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final PdfService pdfService;
    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;

    public DocumentSaveResult saveDocument(
            Long folderId, MultipartFile pdfFile, DocumentSaveRequest documentSaveRequest
    ) {
        String pdfName = pdfService.savePdf(pdfFile);
        String pdfUrl = convertPdfUrl(pdfName);
        Folder folder = folderRepository.getById(folderId);
        Document document = new Document(folder, documentSaveRequest.name(), pdfUrl);
        Document savedDocument = documentRepository.save(document);
        return DocumentSaveResult.of(savedDocument.getId(), savedDocument.getName(), savedDocument.getUrl());
    }

    private String convertPdfUrl(String pdfName) {
        return String.format("pdf/%s", pdfName);
    }
}
