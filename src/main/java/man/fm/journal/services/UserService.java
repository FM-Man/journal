package man.fm.journal.services;

import man.fm.journal.models.datamodels.User;
import man.fm.journal.models.updatePayLoads.UserUpdatePayLoad;
import man.fm.journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUser(ObjectId uid){
        return userRepository.findById(uid);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public List<User> getSomeUser(List<String> usernames){
        List<User> retList = new ArrayList<>();
        List<User> allUserList = getAllUser();
        for (String uname:usernames){
            for (User u:allUserList){
                if(u.getUsername().equals(uname)) {
                    retList.add(u);
                    break;
                }
            }
        }
        return retList;
    }

    public void updateUser(UserUpdatePayLoad payload){
        Optional<User> user = userRepository.findUserByUsername(payload.getUsername());
        if(user.isEmpty()){
            return;
        }
        User newUser = new User(
                user.get().getId(),
                user.get().getUsername(),
                user.get().getPassword(),
                payload.getPostIds(),
                payload.getFollowerIds(),
                payload.getFollowingIds()
        );
        userRepository.save(newUser);
    }
}
