package notai.document.application;

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

    static final String STORAGE_DIR = "src/main/resources/documents/";

    @Test
    void savePdf_success_existsTestPdf() throws IOException {
        //given
        ClassPathResource existsPdf = new ClassPathResource("documents/test.pdf");
        MockMultipartFile mockFile = new MockMultipartFile("file",
                existsPdf.getFilename(),
                "application/pdf",
                Files.readAllBytes(existsPdf.getFile().toPath())
        );
        //when
        String savedFileName = pdfService.savePdf(mockFile);
        //then
        Path savedFilePath = Paths.get(STORAGE_DIR, savedFileName);
        Assertions.assertThat(Files.exists(savedFilePath)).isTrue();

        deleteFile(savedFilePath);
    }

    void deleteFile(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}
