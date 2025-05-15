package com.dl.mapper;

import com.dl.pojo.Category;
import com.dl.pojo.UserRent;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRentMapper {
    //新增分类
    @Insert("insert into category(category_name, category_alias, create_user, create_time, update_time,operator_id) " +
            "values (#{categoryName}, #{categoryAlias}, #{createUser}, #{createTime}, #{updateTime},#{operatorId})")
    void add(UserRent userRent);

    @Select("select * from category where create_user=#{userId}")
    List<UserRent> list(Integer userId);

    @Select("select * from category where id=#{id}")
    UserRent findById(Integer id);

    @Update("update category set category_name=#{categoryName},category_alias=#{categoryAlias}, operator_id=#{operatorId},update_time=#{updateTime} where id=#{id}")
    void update(UserRent userRent);

    @Delete("delete from category where id=#{id}")
    void delete(Integer id);
}
