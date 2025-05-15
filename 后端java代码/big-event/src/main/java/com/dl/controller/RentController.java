package com.dl.controller;

import com.dl.pojo.HouseRent;
import com.dl.pojo.Rent;
import com.dl.pojo.Result;
import com.dl.pojo.User;

import com.dl.service.RentService;
import com.dl.service.UserService;
import com.dl.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rent")
public class RentController {

    @Autowired
    private RentService rentService;
    @PostMapping
    public Result add(@RequestBody @Validated(Rent.Add.class) Rent Rent){


        rentService.add(Rent);
        return Result.success();
    }

    @GetMapping
    public Result<List<Rent>> list(){
        List<Rent> cs = rentService.list();
        return Result.success(cs);
    }

    @GetMapping("/detail")
    public Result<Rent> detail(Integer id){
        Rent c = rentService.findById(id)   ;
        return Result.success(c);
    }

    @PutMapping
    public Result update(@RequestBody @Validated (Rent.Update.class) Rent Rent){
// 从 ThreadLocal 中获取用户信息

        rentService.update(Rent);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id){
        Rent c = rentService.findById(id);
        if(c == null){
            return Result.error("不存在此分类");
        }
        rentService.delete(id);
        return Result.success();
    }






}
