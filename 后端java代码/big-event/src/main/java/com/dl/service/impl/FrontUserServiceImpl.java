package com.dl.service.impl;

import com.dl.mapper.*;
import com.dl.pojo.Contract;
import com.dl.pojo.FrontUser;
import com.dl.pojo.HouseRent;
import com.dl.pojo.User;
import com.dl.service.FrontUserService;
import com.dl.utils.Md5Util;
import com.dl.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FrontUserServiceImpl implements FrontUserService {

    @Autowired
    private FrontUserMapper frontUserMapper;
    @Autowired
    private HouseRentMapper houseRentMapper;

    @Autowired
    private RentOrderMapper rentOrderMapper;

    @Override
    public FrontUser findByUserName(String username) {
        FrontUser frontUser = frontUserMapper.findByUserName(username);
        return frontUser;
    }

    @Override
    public void register(String username, String password) {
        //加密
        String md5string = Md5Util.getMD5String(password);
        //添加用户
        frontUserMapper.add(username, md5string);

    }

    @Override
    public void update(FrontUser frontUser) {



        frontUser.setUpdateTime(LocalDateTime.now());

        frontUserMapper.update(frontUser);
    }


    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        frontUserMapper.updateAvatar(avatarUrl, id);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        frontUserMapper.updatePwd(Md5Util.getMD5String(newPwd), id);
    }

    @Override
    public FrontUser findById(Integer id) {
        return frontUserMapper.findById(id);
    }

    @Override
    public List<FrontUser> findTenantList() {
        return frontUserMapper.findTenantList();
    }


    @Override
    public List<FrontUser> findTenantsByHouseId(Integer houseId) {
        List<Integer> tenantIds = rentOrderMapper.selectTenantIdsByHouseId(houseId);
        if (tenantIds == null || tenantIds.isEmpty()) {
            return new ArrayList<>();
        }
        return frontUserMapper.selectBatchIds(tenantIds);
    }
}
