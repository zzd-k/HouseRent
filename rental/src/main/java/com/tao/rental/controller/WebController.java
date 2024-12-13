package com.tao.rental.controller;

import com.tao.rental.common.Result;
import com.tao.rental.exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

@RestController
public class WebController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/count")
    public Result count(){
        throw new CustomException("你错了哥们","250");


    }

    @GetMapping("/map")
    public Result map(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","墨鱼");
        map.put("age",24);
        return Result.success(map);
    }

}
