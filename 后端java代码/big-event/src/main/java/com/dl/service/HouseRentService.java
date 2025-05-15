package com.dl.service;

import com.dl.pojo.HouseRent;
import com.dl.pojo.PageBean;
import jakarta.transaction.Transactional;

import java.util.List;

public interface HouseRentService {
    //新增文章
    void add(HouseRent houseRent);

    //条件分页列表查询
    PageBean<HouseRent> list(Integer pageNum, Integer pageSize, Integer houserentId, String state, String auditStatus);


    HouseRent findById(Integer id);

    void update(HouseRent houseRent);


    void delete(Integer id);


    void rentHouse(Integer houseId);

    List<HouseRent> getRentedHousesByUser(Integer userId);


    void createOrder(Integer userId, Integer houseId);

    void updateState(Integer id, String state);

    Integer getLandlordId(Integer houseId);

    String getRentStatus(Integer houseId);


    List<HouseRent> getHousesByLandlordId(Integer userId);

    @Transactional
    void forceDeleteByAdmin(Integer id);

    void deleteHouseWithRepairs(Integer id);

    // 更新审核状态
    void updateAuditStatus(Integer id, String auditStatus);

    // 根据审核状态查询房源列表
    PageBean<HouseRent> listByAuditStatus(Integer pageNum, Integer pageSize, String auditStatus);
}
