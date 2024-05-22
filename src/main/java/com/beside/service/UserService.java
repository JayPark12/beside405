package com.beside.service;

import com.beside.DAO.UserDao;
import com.beside.Entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public UserEntity login(String id, String password) {
        return userDao.findUser(id, password);
    }
}
