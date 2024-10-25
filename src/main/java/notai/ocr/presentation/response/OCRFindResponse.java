package notai.ocr.presentation.response;

import notai.ocr.application.result.OCRFindResult;

import java.util.List;

public record OCRFindResponse(
        Long documentId,
        Integer pageNumber,
        List<String> results
) {
    public static OCRFindResponse from(
            OCRFindResult ocrFindResult
    ) {
        return new OCRFindResponse(ocrFindResult.documentId(), ocrFindResult.pageNumber(), ocrFindResult.results());
    }
}
