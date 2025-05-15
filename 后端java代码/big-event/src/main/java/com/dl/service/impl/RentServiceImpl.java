package com.dl.service.impl;

import com.dl.mapper.CategoryMapper;
import com.dl.mapper.HouseRentMapper;
import com.dl.mapper.RentMapper;
import com.dl.pojo.Category;
import com.dl.pojo.HouseRent;
import com.dl.pojo.Rent;
import com.dl.service.RentService;
import com.dl.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RentServiceImpl implements RentService {

    @Autowired
    private HouseRentMapper houseRentMapper;
    @Autowired
    private RentMapper rentMapper;
    @Override
    public void add(Rent rent) {

        // 补充 create_time 和 update_time
        LocalDateTime now = LocalDateTime.now();
        rent.setCreateTime(now); // 新增这一行
        rent.setUpdateTime(now);

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        rent.setCreateUser(id);
        rentMapper.add(rent);
    }

    @Override
//    public List<Rent> list() {
//        Map<String, Object> map = ThreadLocalUtil.get();
//        Integer userId = (Integer) map.get("id");
//        String role = (String) map.get("role"); // 假设角色信息存储在Token中
//        return rentMapper.list(userId);
//
//    }
    // 改为（移除用户ID参数）
    public List<Rent> list() {
        return rentMapper.list();
    }

    @Override
    public Rent findById(Integer id) {
        Rent c = rentMapper.findById(id);
        return c;
    }

    @Override
    public void update(Rent rent) {
        rent.setUpdateTime(LocalDateTime.now());
        rentMapper.update(rent);

    }

    @Override
    public void delete(Integer id) {
        rentMapper.delete(id);
    }




}
