package man.fm.journal.models.updatePayLoads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class PostUpdatePayLoad {
    private String text;
    private ArrayList<String> likerIds;
    private ArrayList<String> dislikerIds;
}
