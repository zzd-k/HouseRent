package com.dl.service;

import com.dl.pojo.Contract;

import java.util.List;
import java.util.Map;

public interface ContractService {
    void add(Contract contract);
    Contract findById(Integer id);
    List<Contract> findByTenantId(Integer tenantId);
    List<Contract> findByHouseId(Integer houseId);


    void signContract(Integer contractId, String role);

    Map<String, Object> findByPage(Integer page, Integer pageSize, String contractTitle, String signStatus);


    List<Contract> findByLandlordAndTenant(Integer landlordId, Integer tenantId);
}
