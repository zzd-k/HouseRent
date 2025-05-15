package com.dl.service.impl;


import com.dl.mapper.ContractMapper;
import com.dl.mapper.HouseRentMapper;
import com.dl.mapper.RentOrderMapper;
import com.dl.mapper.RepairMapper;
import com.dl.pojo.HouseRent;
import com.dl.pojo.PageBean;
import com.dl.service.HouseRentService;
import com.dl.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class HouseRentServiceImpl implements HouseRentService {
    @Autowired
    private HouseRentMapper houseRentMapper;

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private RentOrderMapper rentOrderMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Override
    public void add(HouseRent HouseRent) {
        HouseRent.setCreateTime(LocalDateTime.now());
        HouseRent.setUpdateTime(LocalDateTime.now());
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        HouseRent.setCreateUser(userId);
        houseRentMapper.add(HouseRent);
    }

    @Override
    public PageBean<HouseRent> list(Integer pageNum, Integer pageSize, Integer houserentId, String state, String auditStatus) {

        //创建PageBean对象
        PageBean<HouseRent> pageBean = new PageBean<>();

        //开启分页查询插件
        PageHelper.startPage(pageNum, pageSize);

        //调用mapper
        Map<String, Object> map = ThreadLocalUtil.get();

      List<HouseRent> as = houseRentMapper.list(houserentId, state,auditStatus);
        Page<HouseRent> page = (Page<HouseRent>) as;

        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }

    @Override
    public HouseRent findById(Integer id) {
        HouseRent HouseRent = houseRentMapper.findById(id);
        return HouseRent;
    }

    @Override
    public void update(HouseRent HouseRent) {
        houseRentMapper.update(HouseRent);
    }

    @Override
    public void delete(Integer id) {
        houseRentMapper.delete(id);
    }


    public void rentHouse(Integer houseId) {
        Map<String,Object> user = ThreadLocalUtil.get();
        Integer userId = (Integer) user.get("id");

        // 检查房源是否可租
        HouseRent house = houseRentMapper.findById(houseId);
        if(!"在售".equals(house.getState())){
            throw new RuntimeException("该房源不可租赁");
        }

        // 创建订单
        houseRentMapper.createRentOrder(userId, houseId);

        // 更新房源状态
        houseRentMapper.updateHouseStatus(houseId);
    }


    @Override
    public void createOrder(Integer userId, Integer houseId) {
        // 添加参数校验
        if (userId == null || houseId == null) {
            throw new RuntimeException("用户ID或房屋ID不能为空");
        }

        // 添加完整订单数据（如果需要金额可以从房屋表查询）
        houseRentMapper.createRentOrder(userId, houseId);

        // 同时自动更新房屋状态为已售空
        houseRentMapper.updateState(houseId, "已售空");
    }

    @Override
    public void updateState(Integer id, String state) {
        houseRentMapper.updateState(id, state); // 参数顺序必须与Mapper接口一致
    }

    @Override
    public List<HouseRent> getRentedHousesByUser(Integer userId) {
        return houseRentMapper.findRentedHousesByUser(userId);
    }


    @Override
    public Integer getLandlordId(Integer houseId) {
        HouseRent house = houseRentMapper.findById(houseId);
        if (house == null) {
            throw new RuntimeException("房屋不存在");
        }
        return house.getCreateUser(); // 返回创建用户ID作为房东ID
    }

    @Override
    public String getRentStatus(Integer houseId) {
        HouseRent house = houseRentMapper.findById(houseId);
        if (house == null) {
            throw new RuntimeException("房屋不存在");
        }
        return house.getState(); // 返回当前租赁状态
    }

    // 房东获取自己发布的房源
    public List<HouseRent> getHousesByLandlordId(Integer landlordId) {
        return houseRentMapper.selectHousesByLandlordId(landlordId);
    }



    @Transactional
    @Override
    public void forceDeleteByAdmin(Integer id) {
        // 1. 删除子表
        contractMapper.deleteByHouseId(id);
        rentOrderMapper.deleteByHouseId(id);
        repairMapper.deleteByHouseId(id);

        // 2. 删除主表
        houseRentMapper.delete(id);
    }


    @Transactional
    public void deleteHouseWithRepairs(Integer houseId) {
        // 1. 先删维修记录
        repairMapper.deleteByHouseId(houseId);

        // 2. 再删房源
        houseRentMapper.delete(houseId);
    }


    @Override
    public void updateAuditStatus(Integer id, String auditStatus) {
        houseRentMapper.updateAuditStatus(id, auditStatus);
    }

    // 根据审核状态查询房源列表
    @Override
    public PageBean<HouseRent> listByAuditStatus(Integer pageNum, Integer pageSize, String auditStatus) {
        // 使用 PageHelper 进行分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<HouseRent> list = houseRentMapper.findByAuditStatus(auditStatus);
        Page<HouseRent> page = (Page<HouseRent>) list;
        return new PageBean<>(page.getTotal(), page.getResult());
    }
}
