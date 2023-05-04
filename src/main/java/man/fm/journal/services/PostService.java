package man.fm.journal.services;

import man.fm.journal.models.datamodels.Post;
import man.fm.journal.models.datamodels.User;
import man.fm.journal.models.updatePayLoads.PostUpdatePayLoad;
import man.fm.journal.repository.PostRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPost(){
        return postRepository.findAll();
    }

    public Optional<Post> findPost(String id){
        return postRepository.findById(new ObjectId(id));
    }

    public List<Post> findSomePost(List<String> ids){
        List<Post> retList = new ArrayList<>();
        List<Post> allPostList = getAllPost();
        for (String id:ids){
            for (Post p:allPostList){
                if(p.getPostId().equals(id)) {
                    retList.add(p);
                    break;
                }
            }
        }
        return retList;
    }

    public Optional<Post> updatePost(String id, PostUpdatePayLoad payload){
        Optional<Post> post = postRepository.findById(new ObjectId(id));
        if(post.isEmpty()){
            return Optional.empty();
        }
        post.ifPresent(newPost -> {
            newPost.setText(payload.getText());
            newPost.setLikerIds(payload.getLikerIds());
            newPost.setDislikerIds(payload.getDislikerIds());
            postRepository.save(newPost);
        });
        return post;
    }

    public void createPost(Post newPost){
        postRepository.save(newPost);
    }
}
