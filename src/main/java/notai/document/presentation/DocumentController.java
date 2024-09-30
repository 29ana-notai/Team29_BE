package notai.document.presentation;

import lombok.RequiredArgsConstructor;
import notai.document.application.DocumentService;
import notai.document.application.result.DocumentSaveResult;
import notai.document.presentation.request.DocumentSaveRequest;
import notai.document.presentation.response.DocumentSaveResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@Controller
@RequestMapping("/api/folders/{folderId}/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<DocumentSaveResponse> saveDocument(
            @PathVariable Long folderId,
            @RequestPart MultipartFile pdfFile,
            @RequestPart DocumentSaveRequest documentSaveRequest
    ) {
        DocumentSaveResult documentSaveResult = documentService.saveDocument(folderId, pdfFile, documentSaveRequest);
        DocumentSaveResponse response = DocumentSaveResponse.from(documentSaveResult);
        String url = String.format("/api/folders/%s/documents/%s", folderId, response.id());
        return ResponseEntity.created(URI.create(url)).body(response);
    }
}
