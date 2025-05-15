package com.dl.mapper;


import com.dl.pojo.HouseRent;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HouseRentMapper {
    //新增房屋
    // 新增房源时默认设置为待审核
    @Insert("insert into houserent(title, content, cover_img, state, audit_status, houserent_id, create_user, create_time, update_time, price, area, location, tags, region) " +
            "values (#{title}, #{content}, #{coverImg}, #{state}, '待审核', #{houserentId}, #{createUser}, #{createTime}, #{updateTime}, #{price}, #{area}, #{location}, #{tags}, #{region})")
    void add(HouseRent houserent);


//    List<HouseRent> list(Integer frontId, Integer houserentId, String state);
List<HouseRent> list(@Param("houserentId") Integer houserentId,
                     @Param("state") String state,
                    @Param("auditStatus") String auditStatus);

    @Select("select * from houserent where id=#{id}")
    HouseRent findById(Integer id);

    @Update("update houserent set title=#{title}, content=#{content}, cover_img=#{coverImg}, state=#{state}, price=#{price}, " +
            "houserent_id=#{houserentId} where id=#{id}")
    void update(HouseRent houserent);

    @Delete("DELETE FROM houserent WHERE houserent_id = #{houserentId}")
    void deleteMaintenanceByHouseId(Integer houseId);

    @Delete("delete from houserent where id=#{id}")
    void delete(Integer id);


    @Insert("INSERT INTO rent_order(user_id, house_id, rent_time) VALUES (#{userId}, #{houseId}, NOW())")
    void createRentOrder(@Param("userId") Integer userId, @Param("houseId") Integer houseId);

    @Update("UPDATE houserent SET state = '已售空' WHERE id = #{houseId}")
    void updateHouseStatus(Integer houseId);


    void updateState(@Param("id") Integer id, @Param("state") String state);

    @Select("SELECT hr.* FROM houserent hr " +
            "JOIN rent_order ro ON hr.id = ro.house_id " +
            "WHERE ro.user_id = #{userId}")
    List<HouseRent> findRentedHousesByUser(Integer userId);

    List<HouseRent> findHousesByLandlordWithTenants(Integer landlordId);


    @Select("SELECT * FROM houserent WHERE create_user = #{landlordId}")
    List<HouseRent> selectHousesByLandlordId(Integer landlordId);

    // 更新审核状态
    @Update("update houserent set audit_status = #{auditStatus} where id = #{id}")
    void updateAuditStatus(@Param("id") Integer id, @Param("auditStatus") String auditStatus);

    // 查询审核状态
    @Select("select audit_status from houserent where id = #{id}")
    String findAuditStatusById(Integer id);

    // 根据审核状态查询房源
    @Select("SELECT * FROM houserent WHERE audit_status = #{auditStatus}")
    List<HouseRent> findByAuditStatus(String auditStatus);
}
