package notai.annotation.presentation.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAnnotationRequest {
    @Positive(message = "페이지 번호는 양수여야 합니다.")
    private int pageNumber;
    private int x;
    private int y;
    private int width;
    private int height;
    private String content;
}
