package notai.ocr.application.result;

import java.util.List;

public record OCRFindResult(
        Long documentId,
        Integer pageNumber,
        List<String> results
) {
    public static OCRFindResult of(Long documentId, Integer pageNumber, List<String> results) {
        return new OCRFindResult(documentId, pageNumber, results);
    }
}
