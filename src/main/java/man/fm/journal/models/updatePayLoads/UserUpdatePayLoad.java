package man.fm.journal.models.updatePayLoads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdatePayLoad {
    private String username;
    private ArrayList<String> postIds;
    private ArrayList<String> followerIds;
    private ArrayList<String> followingIds;
}
