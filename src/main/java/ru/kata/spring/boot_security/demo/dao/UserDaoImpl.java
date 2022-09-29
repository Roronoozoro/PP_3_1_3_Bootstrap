package ru.kata.spring.boot_security.demo.dao;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private final EntityManager entityManager;


    public UserDaoImpl(EntityManagerFactory entityManager) {
        this.entityManager = entityManager.createEntityManager();
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void removeUser(Long id) {
        if (findUserById(id) != null) {
            entityManager.remove(findUserById(id));
        }
    }

    @Override
    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findUserByUsername(String username) throws UsernameNotFoundException {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User AS u JOIN FETCH u.roles WHERE u.username= :username", User.class);
        query.setParameter("username", username);
        if (username == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return query.getSingleResult();
    }

    @Override
    public List<User> getListUsers() {
        TypedQuery<User> query = entityManager
                .createQuery("select u from User u", User.class);
        return query.getResultList();
    }

}

