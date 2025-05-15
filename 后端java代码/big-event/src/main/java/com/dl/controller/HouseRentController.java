package com.dl.controller;

import com.dl.mapper.HouseRentMapper;
import com.dl.pojo.*;

import com.dl.service.FrontUserService;
import com.dl.service.HouseRentService;
import com.dl.service.RentOrderService;
import com.dl.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 房源分页控制器
 */
@RestController
@RequestMapping("/houserent")
public class HouseRentController {
    @Autowired
    private HouseRentService houseRentService;
    @Autowired
    private HouseRentMapper houseRentMapper;
    @Autowired
    private RentOrderService rentOrderService;
    @Autowired
    private FrontUserService frontUserService;
    @PostMapping
    public Result add(@RequestBody @Validated HouseRent houseRent){
        houseRentService.add(houseRent);
        return Result.success();
    }
//    @GetMapping
//    public Result<PageBean<HouseRent>> list(@RequestParam(defaultValue = "1") Integer pageNum,
//                                            @RequestParam(defaultValue = "10") Integer pageSize,
//                                            @RequestParam(value = "houserentId", required = false) Integer houserentId,
//                                            @RequestParam(value = "state", required = false) String state){
//
//        PageBean<HouseRent> pageBean = houseRentService.list(pageNum, pageSize, houserentId,state);
//        return Result.success(pageBean);
//    }
    @GetMapping
    public Result<PageBean<HouseRent>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "houserentId", required = false) Integer houserentId,
                                            @RequestParam(value = "state", required = false) String state){

        // 如果是查询房源列表，且没有特别传审核状态，默认过滤掉“待审核”的房源
        String auditStatus = "已通过";  // 默认只显示已通过审核的房源
        PageBean<HouseRent> pageBean = houseRentService.list(pageNum, pageSize, houserentId, state, auditStatus);
        return Result.success(pageBean);
    }

    @GetMapping("/detail")
    public Result<HouseRent> detail(@RequestParam @Validated Integer id){
        HouseRent houseRent = houseRentService.findById(id);
        return Result.success(houseRent);
    }
    // 区域解析方法

    @PutMapping
    public Result update(@RequestBody @Validated HouseRent houseRent){
        houseRentService.update(houseRent);
        return Result.success();
    }

//    @DeleteMapping
//    public Result delete(@RequestParam Integer id) {
//        houseRentService.deleteHouseWithRepairs(id);
//        return Result.success("删除成功");
//    }
@DeleteMapping
public Result delete(@RequestParam Integer id, @RequestParam(required = false) String role) {
    try {
        HouseRent houseRent = houseRentService.findById(id);
        if (houseRent == null) {
            return Result.error("该房屋不存在");
        }

        // 管理员强制删除
        if ("管理员".equals(role)) {
            houseRentService.forceDeleteByAdmin(id);
            return Result.success("管理员已强制删除房源");
        }

        // 普通删除（带外键约束）
        houseRentService.deleteHouseWithRepairs(id);
        return Result.success("删除成功");

    } catch (DataIntegrityViolationException e) {
        return Result.error("该房屋有维修记录，请联系管理员进行相关操作");
    } catch (Exception e) {
        return Result.error("系统错误，删除房屋失败");
    }
}

    @PostMapping("/order")
    public Result createRentOrder(@RequestBody Map<String, Object> orderRequest) {
        Map<String, Object> userInfo = ThreadLocalUtil.get();
        Integer userId = (Integer) userInfo.get("id");

        Integer houseId = (Integer) orderRequest.get("houseId");
        HouseRent house = houseRentService.findById(houseId);

        if (house == null) {
            return Result.error("房源不存在");
        }
        if ("已售空".equals(house.getState())) {
            return Result.error("该房源已出租");
        }

        // 构建订单
        RentOrder order = new RentOrder();
        order.setHouseId(houseId);
        order.setTenantId(userId);
        order.setStartDate(LocalDateTime.now());
        order.setEndDate(LocalDateTime.now().plusMonths(1));
        order.setTotalPrice(house.getPrice());
        order.setOrderStatus("已支付");

        rentOrderService.createOrder(order);

        // 更新房源状态
        houseRentService.updateState(houseId, "已售空");

        // 查询返回 RentOrderVO 给前端
        List<com.dl.vo.RentOrderVO> vos = rentOrderService.getVOListByTenantId(userId);
        com.dl.vo.RentOrderVO latestOrder = vos.get(0); // 取最新的那条（可以优化成按时间排序）

        return Result.success(latestOrder);
    }

    @PutMapping("/state/{id}")
    public Result updateState(@PathVariable Integer id) {
        houseRentService.updateState(id, "已售空");
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<HouseRent> getById(@PathVariable Integer id) {
        HouseRent houseRent = houseRentService.findById(id);
        return Result.success(houseRent);
    }

    @GetMapping("/rented")
    public Result<List<HouseRent>> getRentedHouses() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<HouseRent> houses = houseRentService.getRentedHousesByUser(userId);
        return Result.success(houses);
    }
    // 房东查询自己的房源
    @GetMapping("/landlordHouses/{landlordId}")
    public Result<List<HouseRent>> getLandlordHouses(@PathVariable Integer landlordId) {
        List<HouseRent> houseRentList = houseRentService.getHousesByLandlordId(landlordId);
        if (houseRentList == null || houseRentList.isEmpty()) {
            return Result.success(new ArrayList<>());
        }
        return Result.success(houseRentList);
    }

    // 审核房源：管理员审核房源并更新审核状态
    @PutMapping("/audit/{id}")
    public Result updateAuditStatus(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String auditStatus = request.get("auditStatus");
        houseRentService.updateAuditStatus(id, auditStatus);  // 更新房源审核状态
        return Result.success();
    }

    // 根据审核状态查询房源列表
    @GetMapping("/audit-status")
    public Result<PageBean<HouseRent>> listByAuditStatus(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String auditStatus) {
        PageBean<HouseRent> pageBean = houseRentService.listByAuditStatus(pageNum, pageSize, auditStatus);
        return Result.success(pageBean);
    }
}
