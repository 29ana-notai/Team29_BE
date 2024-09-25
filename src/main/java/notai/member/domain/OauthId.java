package notai.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import static jakarta.persistence.EnumType.STRING;
import jakarta.persistence.Enumerated;
import static lombok.AccessLevel.PROTECTED;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class OauthId {

    @Column(length = 255, nullable = false)
    private String oauthId;

    @Enumerated(STRING)
    @Column(length = 20, nullable = false)
    private OauthProvider oauthProvider;
}
