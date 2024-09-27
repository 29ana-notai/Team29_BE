package notai.folder.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import notai.auth.Auth;
import notai.folder.application.FolderQueryService;
import notai.folder.application.FolderService;
import notai.folder.application.result.FolderResult;
import notai.folder.presentation.request.FolderMoveRequest;
import notai.folder.presentation.request.FolderSaveRequest;
import notai.folder.presentation.response.FolderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;
    private final FolderQueryService folderQueryService;

    @PostMapping
    public ResponseEntity<FolderResponse> saveRootFolder(
            @Auth Long memberId, @Valid @RequestBody FolderSaveRequest folderSaveRequest
    ) {
        FolderResult folderResult;
        if (folderSaveRequest.parentFolderId() != null) {
            folderResult = folderService.saveSubFolder(memberId, folderSaveRequest);
        } else {
            folderResult = folderService.saveRootFolder(memberId, folderSaveRequest);
        }
        var response = FolderResponse.from(folderResult);
        return ResponseEntity.created(URI.create("/api/folders/" + response.id())).body(response);
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<FolderResponse> moveFolder(
            @Auth Long memberId, @PathVariable Long id, @Valid @RequestBody FolderMoveRequest folderMoveRequest
    ) {
        if (folderMoveRequest.targetFolderId() != null) {
            folderService.moveNewParentFolder(memberId, id, folderMoveRequest);
        } else {
            folderService.moveRootFolder(memberId, id);
        }
        return ResponseEntity.ok().build();
    }
}
