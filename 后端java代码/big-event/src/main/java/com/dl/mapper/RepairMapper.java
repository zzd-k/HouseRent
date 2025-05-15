package com.dl.mapper;

import com.dl.pojo.RepairProgress;
import com.dl.pojo.RepairRequest;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RepairMapper {
    // 新增维修请求
    @Insert("INSERT INTO repair_request(house_id, user_id, title, description, images, status, create_time, update_time) " +
            "VALUES (#{houseId}, #{userId}, #{title}, #{description}, #{images}, #{status}, #{createTime}, #{updateTime})")
    void addRequest(RepairRequest request);

    // 查询用户的维修请求
    @Select("SELECT * FROM repair_request WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<RepairRequest> findRequestsByUser(Integer userId);

    // 查询所有维修请求(管理员用)
    @Select("SELECT * FROM repair_request ORDER BY create_time DESC")
    List<RepairRequest> findAllRequests();

    // 查询维修请求详情
    @Select("SELECT * FROM repair_request WHERE id = #{id}")
    RepairRequest findRequestById(Integer id);

    // 更新维修请求状态
    @Update("UPDATE repair_request SET status = #{status}, update_time = #{updateTime} WHERE id = #{id}")
    void updateRequestStatus(Integer id, String status, LocalDateTime updateTime);

    // 添加维修进度记录
    @Insert("INSERT INTO repair_progress(request_id, operator_id, action, details, create_time) " +
            "VALUES (#{requestId}, #{operatorId}, #{action}, #{details}, #{createTime})")
    void addProgress(RepairProgress progress);

    // 查询维修进度记录
    @Select("SELECT * FROM repair_progress WHERE request_id = #{requestId} ORDER BY create_time ASC")
    List<RepairProgress> findProgressByRequest(Integer requestId);

    // 根据房源ID删除所有维修请求
    @Delete("DELETE FROM repair_request WHERE house_id = #{houseId}")
    void deleteByHouseId(Integer houseId);

    @Select("SELECT * FROM repair_request WHERE id = #{id}")
    RepairRequest selectById(Integer id);
    @Update("UPDATE repair_request SET status = #{status}, update_time = #{updateTime} WHERE id = #{id}")
    void updateById(RepairRequest repair);
}