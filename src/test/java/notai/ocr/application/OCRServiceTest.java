package notai.ocr.application;

import notai.document.domain.Document;
import notai.ocr.domain.OCR;
import notai.ocr.domain.OCRRepository;
import notai.pdf.PdfService;
import notai.pdf.result.PdfSaveResult;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class OCRServiceTest {
    @Mock
    OCRRepository ocrRepository;
    @InjectMocks
    PdfService pdfService;
    @InjectMocks
    OCRService ocrService;

    @Test
    void savePdf_success_existsTestPdf() throws IOException {
        //given
        var document = mock(Document.class);
        var ocr = mock(OCR.class);
        ClassPathResource existsPdf = new ClassPathResource("pdf/test.pdf");
        MockMultipartFile mockFile = new MockMultipartFile("file",
                existsPdf.getFilename(),
                "application/pdf",
                Files.readAllBytes(existsPdf.getFile().toPath())
        );
        when(ocrRepository.save(any(OCR.class))).thenReturn(ocr);
        PdfSaveResult saveResult = pdfService.savePdf(mockFile);
        //when, then
        ocrService.saveOCR(document, saveResult.pdf());

        deleteFile(saveResult.pdf().toPath());
    }

    void deleteFile(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}
