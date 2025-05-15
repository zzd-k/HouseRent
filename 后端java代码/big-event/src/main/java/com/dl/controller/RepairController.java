// RepairController.java
package com.dl.controller;

import com.dl.pojo.RepairProgress;
import com.dl.pojo.RepairRequest;
import com.dl.pojo.Result;
import com.dl.service.RepairService;
import com.dl.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repair")
public class RepairController {
    @Autowired
    private RepairService repairService;

    // 提交维修请求
    @PostMapping("/request")
    public Result submitRequest(@RequestBody @Validated RepairRequest request) {

//        System.out.println("接收到的维修请求数据：" + request);
        repairService.submitRequest(request);
        return Result.success();
    }

    // 获取用户维修请求列表
    @GetMapping("/requests")
    public Result<List<RepairRequest>> getUserRequests() {
        // 从ThreadLocal中获取当前用户ID
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        List<RepairRequest> requests = repairService.getUserRequests(userId);
        return Result.success(requests);
    }

    // 获取所有维修请求(管理员)
    @GetMapping("/requests/all")
    public Result<List<RepairRequest>> getAllRequests() {
        List<RepairRequest> requests = repairService.getAllRequests();
        return Result.success(requests);
    }

    // 获取维修请求详情
    @GetMapping("/request/{id}")
    public Result<RepairRequest> getRequestDetails(@PathVariable Integer id) {
        RepairRequest request = repairService.getRequestDetails(id);
        return Result.success(request);
    }

    // 更新维修状态
    @PutMapping("/request/{id}/status")
    public Result updateRequestStatus(@PathVariable Integer id, @RequestParam String status) {
        repairService.updateRequestStatus(id, status);
        return Result.success();
    }

    // 添加维修进度记录
    @PostMapping("/progress")
    public Result addProgressRecord(@RequestBody @Validated RepairProgress progress) {
        repairService.addProgressRecord(progress);
        return Result.success();
    }

    // 获取维修进度记录
    @GetMapping("/progress/{requestId}")
    public Result<List<RepairProgress>> getProgressRecords(@PathVariable Integer requestId) {
        List<RepairProgress> progress = repairService.getProgressRecords(requestId);
        return Result.success(progress);
    }

    @PostMapping("/updateStatus")
    public Result updateStatus(@RequestBody RepairRequest repairRequest) {
        if (repairRequest.getId() == null || repairRequest.getStatus() == null) {
            return Result.error("参数不完整");
        }
        repairService.updateStatus(repairRequest.getId(), repairRequest.getStatus());
        return Result.success("状态更新成功");
    }
}