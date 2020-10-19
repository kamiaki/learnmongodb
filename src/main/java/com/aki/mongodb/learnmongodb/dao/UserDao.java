package com.aki.mongodb.learnmongodb.dao;

import com.aki.mongodb.learnmongodb.po.UserEntity;

import java.util.List;

public interface UserDao {
    /**
     * 创建对象
     *
     * @param user
     */
    public void saveUser(UserEntity user);

    /**
     * 根据用户名查询对象
     *
     * @param userName
     * @return
     */
    public UserEntity findUserByUserName(String userName);

    public List<UserEntity> findall();
    /**
     * 更新对象
     *
     * @param user
     */
    public void updateUser(UserEntity user);

    /**
     * 删除对象
     *
     * @param id
     */
    public void deleteUserById(Long id);
}
