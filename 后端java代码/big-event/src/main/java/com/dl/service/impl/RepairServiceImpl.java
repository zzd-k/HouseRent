package com.dl.service.impl;

import com.dl.mapper.RepairMapper;
import com.dl.pojo.RepairProgress;
import com.dl.pojo.RepairRequest;
import com.dl.service.RepairService;
import com.dl.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
public class RepairServiceImpl implements RepairService {
    @Autowired
    private RepairMapper repairMapper;

    @Override
    public void submitRequest(RepairRequest request) {
        request.setStatus("pending");
        request.setCreateTime(LocalDateTime.now());
        request.setUpdateTime(LocalDateTime.now());

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        request.setUserId(userId);

        repairMapper.addRequest(request);
    }

    @Override
    public List<RepairRequest> getUserRequests(Integer userId) {
        return repairMapper.findRequestsByUser(userId);
    }

    @Override
    public List<RepairRequest> getAllRequests() {
        return repairMapper.findAllRequests();
    }

    @Override
    public RepairRequest getRequestDetails(Integer id) {
        return repairMapper.findRequestById(id);
    }

    @Override
    public void updateRequestStatus(Integer id, String status) {
        repairMapper.updateRequestStatus(id, status, LocalDateTime.now());
    }

    @Override
    public void addProgressRecord(RepairProgress progress) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer operatorId = (Integer) map.get("id");

        progress.setOperatorId(operatorId);
        progress.setCreateTime(LocalDateTime.now());

        repairMapper.addProgress(progress);
    }

    @Override
    public List<RepairProgress> getProgressRecords(Integer requestId) {
        return repairMapper.findProgressByRequest(requestId);
    }

    public void updateStatus(Integer id, String status) {
        RepairRequest repair = repairMapper.selectById(id);
        if (repair != null) {
            repair.setStatus(status);
            repair.setUpdateTime(LocalDateTime.now());
            repairMapper.updateById(repair);
        }
    }
}