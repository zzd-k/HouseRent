package com.dl.service;

import com.dl.pojo.Category;
import com.dl.pojo.UserRent;

import java.util.List;

public interface UserRentService {
    //新增分类
    void add(UserRent userRent);

    //查询所有分类
    List<UserRent> list();

    //查询某分类信息
    UserRent findById(Integer id);

    //更新分类信息
    void update(UserRent userRent);

    //删除分类
    void delete(Integer id);
}
