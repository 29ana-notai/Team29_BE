package notai.annotation.presentation.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAnnotationRequest {
    @Positive(message = "페이지 번호는 양수여야 합니다.")
    private int pageNumber;

    @Min(value = 0, message = "x 좌표는 0 이상이어야 합니다.") // MAX 값이..머지..
    private int x;

    @Min(value = 0, message = "y 좌표는 0 이상이어야 합니다.") // MAX 값이..머지..
    private int y;

    @Positive(message = "withd는 양수여야 합니다.")  // MAX 값이..머지..
    private int width;

    @Positive(message = "height는 양수여야 합니다.")  // MAX 값이..머지..
    private int height;

    private String content;
}
