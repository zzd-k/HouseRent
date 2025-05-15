package com.dl.mapper;

import com.dl.pojo.FrontUser;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FrontUserMapper {
    //根据用户名查找用户
    @Select("select * from front where username=#{username}")
    FrontUser findByUserName(String username);

    //添加用户
    @Insert("insert into front(username, password, create_time, update_time)" +
            " values(#{username},#{password},now(),now())")
    void add(String username, String password);

    @Update("update front set nickname=#{nickname}, email=#{email}, update_time=#{updateTime}, role=#{role} where id=#{id}")
    void update(FrontUser frontUser);

    @Update("update front set user_pic=#{avatarUrl},update_time=now() where id=#{id}")
    void updateAvatar(String avatarUrl, Integer id);

    @Update("update front set password=#{md5String},update_time=now() where id=#{id}")
    void updatePwd(String md5String, Integer id);

    @Select("SELECT * FROM front WHERE id = #{id}")
    FrontUser findById(Integer id);

    List<FrontUser> findTenantList();

    List<FrontUser> selectBatchIds(@Param("ids") List<Integer> ids);
}
