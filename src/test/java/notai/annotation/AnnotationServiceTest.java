package notai.annotation;

import notai.annotation.application.AnnotationService;
import notai.annotation.domain.Annotation;
import notai.annotation.domain.AnnotationRepository;
import notai.annotation.presentation.response.AnnotationResponse;
import notai.document.domain.Document;
import notai.document.domain.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnotationServiceTest {

    @Mock
    private AnnotationRepository annotationRepository;

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private AnnotationService annotationService;

    private Document mockDocument;

    @BeforeEach
    void setUp() {
        // Document의 Mock 객체 생성 및 필드 값 설정
        mockDocument = mock(Document.class);
        when(mockDocument.getId()).thenReturn(1L);  // 문서 ID 설정
    }

    @Test
    void testCreateAnnotation_success() {
        // Mock 데이터 생성
        Annotation annotation = new Annotation(mockDocument, 1, 10, 10, 100, 100, "Content");

        // 현재 시간 설정
        LocalDateTime now = LocalDateTime.now();

        // Reflection으로 createdAt, updatedAt 값 설정
        ReflectionTestUtils.setField(annotation, "createdAt", now);
        ReflectionTestUtils.setField(annotation, "updatedAt", now);

        // Mock 설정: documentRepository에서 ID가 1인 문서를 찾으면 mockDocument 반환
        when(documentRepository.findById(eq(1L))).thenReturn(Optional.of(mockDocument));

        // Mock 설정: annotationRepository가 저장된 Annotation을 반환하도록 설정
        when(annotationRepository.save(any(Annotation.class))).thenReturn(annotation);

        // 서비스 메서드 실행
        AnnotationResponse response = annotationService.createAnnotation(
                1L, 1, 10, 10, 100, 100, "Content"
        );

        // 검증
        verify(annotationRepository, times(1)).save(any(Annotation.class));
        assertNotNull(response);
        assertEquals(1, response.pageNumber());
        assertEquals("Content", response.content());
        assertEquals(now.toString(), response.createdAt());  // 설정한 createdAt이 정상적으로 반환되는지 확인
    }
}