package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BookData {
    @Id
    private Long id;

    private String title;
    private String author;
    private String publisher;
    private Long page;

}
