package man.fm.journal.controllers;

import man.fm.journal.models.datamodels.Post;
import man.fm.journal.models.datamodels.User;
import man.fm.journal.models.updatePayLoads.LikeOrDislikePayLoad;
import man.fm.journal.models.updatePayLoads.PostUpdatePayLoad;
import man.fm.journal.services.PostService;
import man.fm.journal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        return new ResponseEntity<>(
                postService.getAllPost(),
                HttpStatus.OK);
    }

    @GetMapping("/feed/{username}")
    public ResponseEntity<List<Post>> getFeed(@PathVariable String username){
        Optional<User> userNuallable = userService.getUserByUsername(username);

        if(userNuallable.isEmpty()){
            return new ResponseEntity<>(
                    new ArrayList<>(),
                    HttpStatus.NOT_FOUND);
        }

        else {
            User user = userNuallable.get();
            List<String> followingList = user.getFollowingIds();
            List<String> postList = new ArrayList<>();

            for (String uid:followingList){
                Optional<User> following = userService.getUserByUsername(uid);
                postList.addAll(following.get().getPostIds());
            }
            return new ResponseEntity<>(
                    postService.findSomePost( postList ),
                    HttpStatus.OK);
        }
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<List<Post>> getProfile(@PathVariable String username){
        User user = userService.getUserByUsername(username).get();
        List<Post> posts = postService.findSomePost( user.getPostIds() );
        return new ResponseEntity<>( posts, HttpStatus.OK );
    }

    @GetMapping("/individualpost/{postid}")
    public ResponseEntity<Optional<Post>> getindividualPost(@PathVariable String postid){
        Optional<Post> p =postService.findPost(postid);
        return new ResponseEntity<>(p,HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<Post>> updatePost(
            @PathVariable String id,
            @RequestBody PostUpdatePayLoad payload
    ){
        Optional<Post> updatedPost = postService.updatePost(id,payload);
        return new ResponseEntity<>(
                updatedPost,
                HttpStatus.OK);
    }

    @PutMapping("/like")
    public ResponseEntity<Optional<Post>> likePost( @RequestBody LikeOrDislikePayLoad payload ){
        Optional<Post> post = postService.findPost(payload.getPostid());
        if(post.isEmpty())
            return new ResponseEntity<>(Optional.empty(),HttpStatus.NOT_FOUND);

        if(post.get()
                .getLikerIds()
                .contains( payload.getRequester() )
        ){
            post.get()
                    .getLikerIds()
                    .remove( payload.getRequester() );
        }
        else if(post
                .get()
                .getDislikerIds()
                .contains( payload.getRequester() )
        ){
            post.get()
                    .getDislikerIds()
                    .remove( payload.getRequester() );
            post.get()
                    .getLikerIds()
                    .add( payload.getRequester() );
        }
        else {
            post.get()
                    .getLikerIds()
                    .add( payload.getRequester() );
        }
        PostUpdatePayLoad servicePayload = new PostUpdatePayLoad(
                post.get().getText(),
                post.get().getLikerIds(),
                post.get().getDislikerIds());

        return new ResponseEntity<>(
                postService.updatePost(
                        payload.getPostid(),
                        servicePayload),
                HttpStatus.OK);
    }


    @PutMapping("/dislike")
    public ResponseEntity<Optional<Post>> dislikePost( @RequestBody LikeOrDislikePayLoad payload ){
        Optional<Post> post = postService.findPost(payload.getPostid());
        if(post.isEmpty())
            return new ResponseEntity<>(Optional.empty(),HttpStatus.NOT_FOUND);

        if(post.get()
                .getDislikerIds()
                .contains( payload.getRequester() )
        ){
            post.get()
                    .getDislikerIds()
                    .remove( payload.getRequester() );
        }
        else if(post
                .get()
                .getLikerIds()
                .contains( payload.getRequester() )
        ){
            post.get()
                    .getLikerIds()
                    .remove( payload.getRequester() );
            post.get()
                    .getDislikerIds()
                    .add( payload.getRequester() );
        }
        else {
            post.get()
                    .getDislikerIds()
                    .add( payload.getRequester() );
        }
        PostUpdatePayLoad servicePayload = new PostUpdatePayLoad(
                post.get().getText(),
                post.get().getLikerIds(),
                post.get().getDislikerIds());

        return new ResponseEntity<>(
                postService.updatePost(
                        payload.getPostid(),
                        servicePayload),
                HttpStatus.OK);
    }
//    
//    @PutMapping("/post")
//    public ResponseEntity<Optional<Post>> createPost()
}
