package com.dl.service;

import com.dl.pojo.RentOrder;

import java.util.List;

public interface RentOrderService {

    void createOrder(RentOrder rentOrder);

    RentOrder getOrderById(Integer id);

    List<RentOrder> getOrdersByTenantId(Integer tenantId);

    List<RentOrder> getOrdersByHouseId(Integer houseId);

    List<RentOrder> getAllOrders();

    void deleteOrder(Integer id);

    void updateOrder(RentOrder rentOrder);

    List<com.dl.vo.RentOrderVO> getVOListByTenantId(Integer tenantId);

    com.dl.vo.RentOrderVO getOrderDetail(Integer id);
}
