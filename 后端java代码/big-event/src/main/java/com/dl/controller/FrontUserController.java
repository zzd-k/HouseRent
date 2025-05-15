package com.dl.controller;

import com.dl.pojo.FrontUser;
import com.dl.pojo.Result;
import com.dl.pojo.User;
import com.dl.service.FrontUserService;
import com.dl.utils.JwtUtil;
import com.dl.utils.Md5Util;
import com.dl.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/front")
@Validated
public class FrontUserController {

    @Autowired
    private FrontUserService frontUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$",message = "用户名必须是5-16位非空字符") String username,
                           @Pattern(regexp = "^\\S{5,16}$",message = "密码必须是5-16位非空字符") String password){
        //查询用户
        FrontUser frontUser = frontUserService.findByUserName(username);
        if (frontUser == null){
            //用户不存在,注册
            frontUserService.register(username, password);
            return Result.success();
        }else{
            //用户已存在
            return Result.error("用户已经存在");
        }
    }


    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$",message = "用户名必须是5-16位非空字符") String username,
                                @Pattern(regexp = "^\\S{5,16}$",message = "密码必须是5-16位非空字符") String password){
        FrontUser loginUser = frontUserService.findByUserName(username);

        if(loginUser == null){
            return Result.error("用户名错误");
        }

        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            //登陆成功
            Map<String,Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 1, TimeUnit.HOURS);
            return Result.success(token);
        }

        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<FrontUser> userInfo(){
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        FrontUser frontUser = frontUserService.findByUserName(username);
        return Result.success(frontUser);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated FrontUser frontUser){
        frontUserService.update(frontUser);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        frontUserService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token){
        //校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少参数");
        }
        //校验原密码
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        FrontUser frontUser = frontUserService.findByUserName(username);
        if(!frontUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码输入错误");
        }
        //校验新密码和确认密码是否相同
        if(!newPwd.equals(rePwd)){
            return Result.error("确认密码不一致");
        }
        frontUserService.updatePwd(newPwd);

        //密码更新后要删除旧token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<FrontUser> getUserById(@PathVariable Integer id) {
        FrontUser frontUser = frontUserService.findById(id);
        if (frontUser != null) {
            return Result.success(frontUser);
        } else {
            return Result.error("用户不存在");
        }
    }

    @GetMapping("/tenantList")
    public Result<List<FrontUser>> getTenantList() {
        List<FrontUser> tenantList = frontUserService.findTenantList();
        return Result.success(tenantList);
    }

    @GetMapping("/tenantListByHouse/{houseId}")
    public Result<List<FrontUser>> getTenantListByHouse(@PathVariable Integer houseId) {
        List<FrontUser> tenantList = frontUserService.findTenantsByHouseId(houseId);
        return Result.success(tenantList);
    }

}
