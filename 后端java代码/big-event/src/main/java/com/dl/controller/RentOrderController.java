package com.dl.controller;

import com.dl.pojo.RentOrder;
import com.dl.service.RentOrderService;
import com.dl.pojo.Result;
import com.dl.utils.ThreadLocalUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rentOrder")
public class RentOrderController {

    @Autowired
    private RentOrderService rentOrderService;

    @PostMapping("/create")
    public Result create(@RequestBody RentOrder rentOrder) {
        rentOrderService.createOrder(rentOrder);
        return Result.success();
    }

    @GetMapping("/tenant/{tenantId}")
    public Result<PageInfo<com.dl.vo.RentOrderVO>> listByTenant(
            @PathVariable Integer tenantId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Map<String, Object> userInfo = ThreadLocalUtil.get();
        Integer userId = (Integer) userInfo.get("id");

        PageHelper.startPage(pageNum, pageSize);
        List<com.dl.vo.RentOrderVO> list = rentOrderService.getVOListByTenantId(userId);

        return Result.success(new PageInfo<>(list));
    }
    @GetMapping("/house/{houseId}")
    public Result<List<RentOrder>> listByHouse(@PathVariable Integer houseId) {
        List<RentOrder> list = rentOrderService.getOrdersByHouseId(houseId);
        return Result.success(list);
    }

    @GetMapping("/all")
    public Result<List<RentOrder>> listAll() {
        return Result.success(rentOrderService.getAllOrders());
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        rentOrderService.deleteOrder(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody RentOrder rentOrder) {
        rentOrderService.updateOrder(rentOrder);
        return Result.success();
    }


    @GetMapping("/detail/{id}")
    public Result<com.dl.vo.RentOrderVO> getOrderDetail(@PathVariable Integer id) {
        com.dl.vo.RentOrderVO order = rentOrderService.getOrderDetail(id);
        return Result.success(order);
    }
}
