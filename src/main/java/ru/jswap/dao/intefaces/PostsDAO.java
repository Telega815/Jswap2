package ru.jswap.dao.intefaces;

import ru.jswap.entities.Feeds;
import ru.jswap.entities.Post;

import java.util.List;

public interface PostsDAO {

    List<Post> getPosts();
    List<Post> getPosts(Feeds feed);
    Post getPost(Long post_pk);
    Long savePost(Post post);
    void deletePost(Post post);
    void updatePost(Post post);
}
