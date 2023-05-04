package man.fm.journal.models.updatePayLoads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePostPayload {
    private String poster;
    private String text;
}
