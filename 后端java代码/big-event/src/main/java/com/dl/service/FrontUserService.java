package com.dl.service;

import com.dl.pojo.FrontUser;

import java.util.List;


public interface FrontUserService {
    //根据用户名查找用户
    FrontUser findByUserName(String username);

    //注册
    void register(String username, String password);

    //更新
    void update(FrontUser frontUser);

    //更新头像
    void updateAvatar(String avatarUrl);

    //更新密码
    void updatePwd(String newPwd);

    FrontUser findById(Integer id);
    List<FrontUser> findTenantList();

    List<FrontUser> findTenantsByHouseId(Integer houseId);
}
