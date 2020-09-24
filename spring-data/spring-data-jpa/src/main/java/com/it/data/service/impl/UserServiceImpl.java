package com.it.data.service.impl;

import com.it.data.bean.User;
import com.it.data.dao.UserDao;
import com.it.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    public User getUserById(Long userId) {
        return this.userDao.getOne(userId);
    }

    public void addUser(User user) {
          this.userDao.save(user);
    }



}
