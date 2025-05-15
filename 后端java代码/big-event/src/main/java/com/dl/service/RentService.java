package com.dl.service;

import com.dl.pojo.Category;
import com.dl.pojo.Rent;

import java.util.List;

public interface RentService {
    //新增分类
    void add(Rent rent);

    //查询所有分类
    List<Rent> list();

    //查询某分类信息
    Rent findById(Integer id);

    //更新分类信息
    void update(Rent rent);

    //删除分类
    void delete(Integer id);




}
