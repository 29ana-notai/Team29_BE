package notai.ocr.domain;

import static notai.common.exception.ErrorMessages.OCR_RESULT_NOT_FOUND;
import notai.common.exception.type.NotFoundException;
import notai.document.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OCRRepository extends JpaRepository<OCR, Long> {
    default OCR getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(OCR_RESULT_NOT_FOUND));
    }

    List<OCR> findOCRByDocumentIdAndPageNumber(Long documentId, Integer pageNumber);

    void deleteAllByDocument(Document document);
}
