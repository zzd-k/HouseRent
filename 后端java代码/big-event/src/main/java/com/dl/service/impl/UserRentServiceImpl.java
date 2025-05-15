package com.dl.service.impl;

import com.dl.mapper.CategoryMapper;
import com.dl.mapper.UserRentMapper;

import com.dl.pojo.UserRent;
import com.dl.service.UserRentService;
import com.dl.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserRentServiceImpl implements UserRentService {
    @Autowired
    private UserRentMapper userRentMapper;
    @Override
    public void add(UserRent userRent) {
        //补充属性值
        userRent.setUpdateTime(LocalDateTime.now());
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userRent.setCreateUser(id);

        userRentMapper.add(userRent);
    }



    @Override
    public List<UserRent> list() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return userRentMapper.list(userId);
    }

    @Override
    public UserRent findById(Integer id) {
        UserRent c = userRentMapper.findById(id);
        return c;
    }

    @Override
    public void update(UserRent userRent) {
        userRent.setUpdateTime(LocalDateTime.now());
        userRentMapper.update(userRent);

    }

    @Override
    public void delete(Integer id) {
        userRentMapper.delete(id);
    }
}
