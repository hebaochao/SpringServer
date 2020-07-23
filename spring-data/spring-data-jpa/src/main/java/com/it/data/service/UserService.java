package com.it.data.service;

import com.it.data.bean.User;

public interface UserService {


    public User getUserById(Long userId);

    public void  addUser(User user);


}
