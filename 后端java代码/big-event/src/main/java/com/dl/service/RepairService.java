package com.dl.service;

import com.dl.pojo.RepairProgress;
import com.dl.pojo.RepairRequest;

import java.util.List;

public interface RepairService {
    // 提交维修请求
    void submitRequest(RepairRequest request);

    // 获取用户维修请求列表
    List<RepairRequest> getUserRequests(Integer userId);

    // 获取所有维修请求(管理员)
    List<RepairRequest> getAllRequests();

    // 获取维修请求详情
    RepairRequest getRequestDetails(Integer id);

    // 更新维修状态
    void updateRequestStatus(Integer id, String status);

    // 添加维修进度记录
    void addProgressRecord(RepairProgress progress);

    // 获取维修进度记录
    List<RepairProgress> getProgressRecords(Integer requestId);

    void updateStatus(Integer id, String status);
}
