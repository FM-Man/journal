package man.fm.journal.controllers;

import man.fm.journal.models.datamodels.User;
import man.fm.journal.models.updatePayLoads.FollowOrUnfollowPayLoad;
import man.fm.journal.models.updatePayLoads.UserUpdatePayLoad;
import man.fm.journal.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserContrller {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        return new ResponseEntity<List<User>>(userService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/{userid}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable String userid){
        return new ResponseEntity<Optional<User>>(userService.getUser(new ObjectId(userid)),HttpStatus.OK);
    }

    @GetMapping("/uname/{username}")
    public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable String username){
        return new ResponseEntity<>(userService.getUserByUsername(username),HttpStatus.OK);
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<List<User>> getAllFollowers(@PathVariable String username){
        Optional<User> u = userService.getUserByUsername(username);
        return new ResponseEntity<>(userService.getSomeUser(u.get().getFollowerIds()), HttpStatus.OK);
    }

    @GetMapping("/followings/{username}")
    public ResponseEntity<List<User>> getAllFollowings(@PathVariable String username){
        Optional<User> u = userService.getUserByUsername(username);
        return new ResponseEntity<>(userService.getSomeUser(u.get().getFollowingIds()), HttpStatus.OK);
    }

    @PutMapping("/follow")
    public ResponseEntity<User> followUser(@RequestBody FollowOrUnfollowPayLoad payload){
        Optional<User> target = userService.getUserByUsername(payload.getTarget());
        Optional<User> requester = userService.getUserByUsername(payload.getRequester());

        if(target.isEmpty() || requester.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(target.get()
                .getFollowerIds()
                .contains(payload.getRequester()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        target.get()
                .getFollowerIds()
                .add(payload.getRequester() );

        requester.get()
                .getFollowingIds()
                .add( payload.getTarget() );

        userService.updateUser( new UserUpdatePayLoad(
                target.get().getUsername(),
                target.get().getPostIds(),
                target.get().getFollowerIds(),
                target.get().getFollowingIds()
        ));

        userService.updateUser( new UserUpdatePayLoad(
                requester.get().getUsername(),
                requester.get().getPostIds(),
                requester.get().getFollowerIds(),
                requester.get().getFollowingIds()
        ));

        return new ResponseEntity<>(
                target.get(),
                HttpStatus.OK);
    }

    @PutMapping("/unfollow")
    public ResponseEntity<User> unfollowUser(@RequestBody FollowOrUnfollowPayLoad payload){
        Optional<User> target = userService.getUserByUsername(payload.getTarget());
        Optional<User> requester = userService.getUserByUsername(payload.getRequester());

        if(target.isEmpty() || requester.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(!target.get()
                .getFollowerIds()
                .contains(payload.getRequester()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        target.get()
                .getFollowerIds()
                .remove(payload.getRequester());

        requester.get()
                .getFollowingIds()
                .remove(payload.getTarget());

        userService.updateUser( new UserUpdatePayLoad(
                target.get().getUsername(),
                target.get().getPostIds(),
                target.get().getFollowerIds(),
                target.get().getFollowingIds()
        ));

        userService.updateUser( new UserUpdatePayLoad(
                requester.get().getUsername(),
                requester.get().getPostIds(),
                requester.get().getFollowerIds(),
                requester.get().getFollowingIds()
        ));

        return new ResponseEntity<>(
                target.get(),
                HttpStatus.OK);
    }
}
