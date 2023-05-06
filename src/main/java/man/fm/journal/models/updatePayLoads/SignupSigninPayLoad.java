package man.fm.journal.models.updatePayLoads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupSigninPayLoad {
    private String username;
    private String password;
}
