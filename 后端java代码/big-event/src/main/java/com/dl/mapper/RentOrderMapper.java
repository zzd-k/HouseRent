package com.dl.mapper;

import com.dl.pojo.RentOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RentOrderMapper {

    @Select("SELECT DISTINCT tenant_id FROM rent_order WHERE house_id = #{houseId}")
    List<Integer> selectTenantIdsByHouseId(@Param("houseId") Integer houseId);

    @Insert("INSERT INTO rent_order(house_id, tenant_id, start_date, end_date, total_price, order_status, create_time, update_time) " +
            "VALUES (#{houseId}, #{tenantId}, #{startDate}, #{endDate}, #{totalPrice}, #{orderStatus}, NOW(), NOW())")
    void insert(RentOrder rentOrder);

    @Select("SELECT * FROM rent_order WHERE id = #{id}")
    RentOrder findById(Integer id);

    @Select("SELECT * FROM rent_order WHERE tenant_id = #{tenantId}")
    List<RentOrder> findByTenantId(Integer tenantId);

    @Select("SELECT * FROM rent_order WHERE house_id = #{houseId}")
    List<RentOrder> findByHouseId(Integer houseId);

    @Select("SELECT * FROM rent_order")
    List<RentOrder> findAll();

    @Delete("DELETE FROM rent_order WHERE id = #{id}")
    void delete(Integer id);

    @Update("UPDATE rent_order SET house_id = #{houseId}, tenant_id = #{tenantId}, start_date = #{startDate}, " +
            "end_date = #{endDate}, total_price = #{totalPrice}, order_status = #{orderStatus}, update_time = NOW() " +
            "WHERE id = #{id}")
    void update(RentOrder rentOrder);
    @Select("SELECT ro.*, hr.title, hr.cover_img " +
            "FROM rent_order ro " +
            "LEFT JOIN houserent hr ON ro.house_id = hr.id " +
            "WHERE ro.tenant_id = #{tenantId}")
    List<com.dl.vo.RentOrderVO> findVOByTenantId(Integer tenantId);

    @Select("SELECT ro.*, hr.title, hr.cover_img, hr.price as house_price " +
            "FROM rent_order ro " +
            "LEFT JOIN houserent hr ON ro.house_id = hr.id " +
            "WHERE ro.id = #{orderId}")
    com.dl.vo.RentOrderVO findVOById(Integer orderId);

    @Delete("DELETE FROM rent_order WHERE house_id = #{houseId}")
    void deleteByHouseId(@Param("houseId") Integer houseId);
}
