package notai.document.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/folders/{folderId}/documents")
@RequiredArgsConstructor
public class DocumentController {
}
