package ru.jswap.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.jswap.dao.intefaces.FilePathDAO;
import ru.jswap.entities.FilePath;

@Component
public class FilePathDAOImpl implements FilePathDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public FilePath getPath(Integer id) {
        return sessionFactory.getCurrentSession().get(FilePath.class, id);
    }

    @Transactional
    @Override
    public int savePath(FilePath path) {
        return (int)sessionFactory.getCurrentSession().save(path);
    }

    @Transactional
    @Override
    public void deletePath(FilePath path) {
        sessionFactory.getCurrentSession().delete(path);
    }

    @Transactional
    @Override
    public void deletePath(Integer id) {
        sessionFactory.getCurrentSession().createQuery("delete from FilePath where id = :id").setParameter("id", id);
    }
}
