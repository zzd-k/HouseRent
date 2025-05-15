package com.dl.mapper;

import com.dl.pojo.Category;
import com.dl.pojo.Rent;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RentMapper {
    //新增分类
    @Insert("insert into rent(rent_name, rent_alias, create_user, create_time, update_time) " +
            "values (#{rentName}, #{rentAlias}, #{createUser}, #{createTime}, #{updateTime})")
    void add(Rent rent);

//    @Select("select * from rent where create_user=#{userId}")
//    List<Rent> list(Integer userId);
// 改为（移除用户过滤条件）
@Select("select * from rent")
List<Rent> list();


    @Select("SELECT * FROM rent")
    List<Rent> listAll();


    @Select("select * from rent where id=#{id}")
    Rent findById(Integer id);

    @Update("update rent set rent_name=#{rentName},rent_alias=#{rentAlias},update_time=#{updateTime} where id=#{id}")
    void update(Rent rent);

    @Delete("delete from rent where id=#{id}")
    void delete(Integer id);



}
