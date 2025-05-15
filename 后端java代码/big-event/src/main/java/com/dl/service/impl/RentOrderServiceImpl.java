package com.dl.service.impl;

import com.dl.mapper.RentOrderMapper;
import com.dl.pojo.RentOrder;
import com.dl.service.RentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentOrderServiceImpl implements RentOrderService {

    @Autowired
    private RentOrderMapper rentOrderMapper;

    @Override
    public void createOrder(RentOrder rentOrder) {
        rentOrderMapper.insert(rentOrder);
    }

    @Override
    public RentOrder getOrderById(Integer id) {
        return rentOrderMapper.findById(id);
    }

    @Override
    public List<RentOrder> getOrdersByTenantId(Integer tenantId) {
        return rentOrderMapper.findByTenantId(tenantId);
    }

    @Override
    public List<RentOrder> getOrdersByHouseId(Integer houseId) {
        return rentOrderMapper.findByHouseId(houseId);
    }

    @Override
    public List<RentOrder> getAllOrders() {
        return rentOrderMapper.findAll();
    }

    @Override
    public void deleteOrder(Integer id) {
        rentOrderMapper.delete(id);
    }

    @Override
    public void updateOrder(RentOrder rentOrder) {
        rentOrderMapper.update(rentOrder);
    }

    @Override
    public List<com.dl.vo.RentOrderVO> getVOListByTenantId(Integer tenantId) {
        return rentOrderMapper.findVOByTenantId(tenantId);
    }
    public com.dl.vo.RentOrderVO getOrderDetail(Integer orderId) {
        return rentOrderMapper.findVOById(orderId);
    }
}
