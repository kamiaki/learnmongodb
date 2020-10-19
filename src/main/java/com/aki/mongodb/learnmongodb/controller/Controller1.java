package com.aki.mongodb.learnmongodb.controller;

import com.aki.mongodb.learnmongodb.dao.UserDao;
import com.aki.mongodb.learnmongodb.po.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
public class Controller1 {
    @Autowired
    UserDao userDao;

    @RequestMapping(value = "c")
    public String  c(){
        UserEntity userEntity = new UserEntity();
        Long aLong = Long.valueOf(new Random().nextInt(1000));
        userEntity.setId(aLong);
        userEntity.setUserName("aki");
        userEntity.setPassWord("462580");
        userDao.saveUser(userEntity);
        return "添加:" + userEntity.toString();
    }
    @RequestMapping(value = "u")
    public String  u(){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(199L);
        userEntity.setUserName("aki");
        userEntity.setPassWord("修改后的数据");
        userDao.updateUser(userEntity);
        return "修改:" + userEntity.toString();
    }
    @RequestMapping(value = "r")
    public String r(){
        List<UserEntity> userEntities = userDao.findall();
        return "查询:" + userEntities.toString();
    }

    @RequestMapping(value = "d")
    public String  d(){
        userDao.deleteUserById(260L);
        return "删除:" + 260L;

    }
}
