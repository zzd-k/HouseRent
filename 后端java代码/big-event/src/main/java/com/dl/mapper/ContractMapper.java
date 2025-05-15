
        package com.dl.mapper;

import com.dl.pojo.Contract;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ContractMapper {
    @Insert("INSERT INTO contract(contract_title, house_id, tenant_id,landlord_id, content, sign_status, create_time, update_time) " +
            "VALUES (#{contractTitle}, #{houseId}, #{tenantId},#{landlordId}, #{content}, #{signStatus}, #{createTime}, #{updateTime})")
    void add(Contract contract);

    @Select("SELECT * FROM contract WHERE id = #{id}")
    Contract findById(Integer id);

    @Select("SELECT * FROM contract WHERE tenant_id = #{tenantId}")
    List<Contract> findByTenantId(Integer tenantId);

    @Select("SELECT * FROM contract WHERE house_id = #{houseId}")
    List<Contract> findByHouseId(Integer houseId);

    @Update("UPDATE contract SET sign_status = #{signStatus}, signed_time = #{signedTime}, update_time = NOW() WHERE id = #{id}")
    void updateSignStatus(@Param("id") Integer id, @Param("signStatus") String signStatus, @Param("signedTime") LocalDateTime signedTime);


    List<Contract> findByCondition(String contractTitle, String signStatus);

    @Select("SELECT * FROM contract WHERE landlord_id = #{landlordId}")
    List<Contract> findByLandlordId(Integer landlordId);
    void update(Contract contract);

    @Select("SELECT c.*, hr.title as houseName, f.username as tenantName " +
            "FROM contract c " +
            "JOIN houserent hr ON c.house_id = hr.id " +
            "JOIN front f ON c.tenant_id = f.id " +
            "WHERE c.landlord_id = #{landlordId} AND c.tenant_id = #{tenantId}")
    List<Contract> findByLandlordAndTenant(@Param("landlordId") Integer landlordId,
                                           @Param("tenantId") Integer tenantId);

    @Delete("DELETE FROM contract WHERE house_id = #{houseId}")
    void deleteByHouseId(@Param("houseId") Integer houseId);
}