package notai.annotation.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents/{documentId}/annotations")
@RequiredArgsConstructor
public class AnnotationController {
}
