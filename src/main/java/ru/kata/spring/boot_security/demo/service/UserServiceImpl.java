package ru.kata.spring.boot_security.demo.service;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    final PasswordEncoder encoder;


    public UserServiceImpl(UserDao userDao, RoleDao roleDao, @Lazy PasswordEncoder encoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleDao.getRoleByName("ROLE_USER")));
        this.userDao.addUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(roleDao.getRolesByName(user.getRoles()));
        this.userDao.updateUser(user);
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        this.userDao.removeUser(id);
    }

    @Override
    public User findUserById(Long id) {
        return this.userDao.findUserById(id);
    }

    @Override
    public List<User> getListUsers() {
        return this.userDao.getListUsers();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }

}