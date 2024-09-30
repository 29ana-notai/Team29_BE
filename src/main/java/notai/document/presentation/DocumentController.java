package notai.document.presentation;

import lombok.RequiredArgsConstructor;
import notai.document.application.DocumentService;
import notai.document.application.result.DocumentSaveResult;
import notai.document.application.result.DocumentUpdateResult;
import notai.document.presentation.request.DocumentSaveRequest;
import notai.document.presentation.request.DocumentUpdateRequest;
import notai.document.presentation.response.DocumentSaveResponse;
import notai.document.presentation.response.DocumentUpdateResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping(value = "/{id}")
    public ResponseEntity<DocumentUpdateResponse> updateDocument(
            @PathVariable Long folderId, @PathVariable Long id, @RequestBody DocumentUpdateRequest documentUpdateRequest
    ) {
        DocumentUpdateResult documentUpdateResult = documentService.updateDocument(folderId, id, documentUpdateRequest);
        DocumentUpdateResponse response = DocumentUpdateResponse.from(documentUpdateResult);
        return ResponseEntity.ok(response);
    }
}
