package notai.ocr.application;

import lombok.RequiredArgsConstructor;
import notai.ocr.application.result.OCRFindResult;
import notai.ocr.domain.OCR;
import notai.ocr.domain.OCRRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OCRQueryService {

    private final OCRRepository ocrRepository;

    public OCRFindResult findOCR(Long documentId, Integer pageNumber) {
        List<OCR> ocrs = ocrRepository.findOCRByDocumentIdAndPageNumber(documentId, pageNumber);

        return getOCRFindResult(documentId, pageNumber, ocrs);
    }

    private OCRFindResult getOCRFindResult(Long documentId, Integer pageNumber, List<OCR> ocrs) {
        List<String> results = ocrs.stream().map(OCR::getContent).toList();
        return OCRFindResult.of(documentId, pageNumber, results);
    }
}
