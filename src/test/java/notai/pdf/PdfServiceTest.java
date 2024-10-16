package notai.pdf;

import notai.pdf.result.PdfSaveResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    @InjectMocks
    PdfService pdfService;
    static final String PATH_FORMAT = "src/main/resources/pdf/%s";

    @Test
    void savePDF_success_existsTestPdf() throws IOException {
        //given
        ClassPathResource existsPdf = new ClassPathResource("pdf/test.pdf");
        MockMultipartFile mockFile = new MockMultipartFile("file",
                existsPdf.getFilename(),
                "application/pdf",
                Files.readAllBytes(existsPdf.getFile().toPath())
        );
        //when
        PdfSaveResult saveResult = pdfService.savePdf(mockFile);
        //then
        Path filePath = Paths.get(String.format(PATH_FORMAT, saveResult.pdfName()));
        Assertions.assertThat(filePath.toFile().exists()).isTrue();

        deleteFile(filePath);
    }

    void deleteFile(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}
