package man.fm.journal.models.datamodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")     //to say its a model from users
@Data                               //to write the getters and setters
@AllArgsConstructor                 //for constructor
@NoArgsConstructor                  //empty constructors
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private ArrayList<String> postIds;
    private ArrayList<String> followerIds;
    private ArrayList<String> followingIds;
}
