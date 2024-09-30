package notai.document.domain;

import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.validation.constraints.NotNull;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notai.common.domain.RootEntity;
import notai.folder.domain.Folder;

@Entity
@Table(name = "document")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Document extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", referencedColumnName = "id")
    private Folder folder;

    @NotNull
    @Column(name = "name", length = 50)
    private String name;

    @NotNull
    @Column(name = "url")
    private String url;

    public Document(Folder folder, String name, String url) {
        this.folder = folder;
        this.name = name;
        this.url = url;
    }
}
