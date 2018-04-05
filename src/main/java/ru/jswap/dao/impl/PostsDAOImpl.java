package ru.jswap.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.jswap.dao.intefaces.PostsDAO;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.Post;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class PostsDAOImpl implements PostsDAO {

    @Autowired
    SessionFactory sessionFactory;

    List<Post> posts;

    @Transactional
    @Override
    public List<Post> getPosts() {
        posts = sessionFactory.getCurrentSession()
                .createCriteria(Post.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        return posts;
    }

    @Transactional
    @Override
    public List<Post> getPosts(Feeds feed) {
        posts = sessionFactory.getCurrentSession().createQuery("from Post where feed = :feed").setParameter("feed", feed).list();
        return posts;
    }

    @Transactional
    @Override
    public Post getPost(Long post_pk) {
        return sessionFactory.getCurrentSession().get(Post.class, post_pk);
    }

    @Transactional
    @Override
    public Long savePost(Post post) {
        return (long)sessionFactory.getCurrentSession().save(post);
    }

    @Transactional
    @Override
    public void deletePost(Post post) {
        sessionFactory.getCurrentSession().delete(post);
    }

    @Transactional
    @Override
    public void updeatePost(Post post) {
        sessionFactory.getCurrentSession().update(post);
    }
}
