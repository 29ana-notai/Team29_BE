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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;
    private final FolderQueryService folderQueryService;

    @PostMapping
    public ResponseEntity<FolderResponse> saveFolder(
            @Auth Long memberId, @Valid @RequestBody FolderSaveRequest folderSaveRequest
    ) {
        var folderResult = saveFolderResult(memberId, folderSaveRequest);
        var response = FolderResponse.from(folderResult);
        return ResponseEntity.created(URI.create("/api/folders/" + response.id())).body(response);
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<FolderResponse> moveFolder(
            @Auth Long memberId, @PathVariable Long id, @Valid @RequestBody FolderMoveRequest folderMoveRequest
    ) {
        moveFolderWithRequest(memberId, id, folderMoveRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FolderResponse>> getFolders(
            @Auth Long memberId, @RequestParam(required = false) Long parentFolderId
    ) {
        var folderResults = folderQueryService.getFolders(memberId, parentFolderId);
        var response = folderResults.stream().map(FolderResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<FolderResponse>> deleteFolder(
            @Auth Long memberId, @PathVariable Long id
    ) {
        folderService.deleteFolder(memberId, id);
        return ResponseEntity.noContent().build();
    }

    private FolderResult saveFolderResult(Long memberId, FolderSaveRequest folderSaveRequest) {
        if (folderSaveRequest.parentFolderId() != null) {
            return folderService.saveSubFolder(memberId, folderSaveRequest);
        }
        return folderService.saveRootFolder(memberId, folderSaveRequest);
    }

    private void moveFolderWithRequest(Long memberId, Long id, FolderMoveRequest folderMoveRequest) {
        if (folderMoveRequest.targetFolderId() != null) {
            folderService.moveNewParentFolder(memberId, id, folderMoveRequest);
            return;
        }
        folderService.moveRootFolder(memberId, id);
    }
}
