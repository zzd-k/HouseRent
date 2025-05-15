package com.dl.service.impl;

import com.dl.mapper.ContractMapper;
import com.dl.mapper.HouseRentMapper;
import com.dl.pojo.Contract;
import com.dl.service.ContractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private HouseRentMapper houseRentMapper;
    @Override
    public void add(Contract contract) {
        contract.setCreateTime(LocalDateTime.now());
        contract.setUpdateTime(LocalDateTime.now());
        contract.setSignStatus("未签署");
        contractMapper.add(contract);
    }

    @Override
    public Contract findById(Integer id) {
        return contractMapper.findById(id);
    }

    @Override
    public List<Contract> findByTenantId(Integer tenantId) {
        return contractMapper.findByTenantId(tenantId);
    }

    @Override
    public List<Contract> findByHouseId(Integer houseId) {
        return contractMapper.findByHouseId(houseId);
    }



    @Override
    public void signContract(Integer contractId, String role) {
        Contract contract = contractMapper.findById(contractId);
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }

        boolean updated = false;
        if ("landlord".equals(role) && !contract.getLandlordSigned()) {
            contract.setLandlordSigned(true);
            updated = true;
        } else if ("tenant".equals(role) && !contract.getTenantSigned()) {
            contract.setTenantSigned(true);
            updated = true;
        }

        if (updated) {
            contract.setUpdateTime(LocalDateTime.now());

            // 如果双方都签了，才标记为“已签署”
            if (contract.getLandlordSigned() && contract.getTenantSigned()) {
                contract.setSignStatus("已签署");
                contract.setSignedTime(LocalDateTime.now());
            }

            contractMapper.update(contract);  // 提醒你：你需要有这个 update 方法
        }
    }

    @Override
    public Map<String, Object> findByPage(Integer page, Integer pageSize, String contractTitle, String signStatus) {
        PageHelper.startPage(page, pageSize);
        List<Contract> list = contractMapper.findByCondition(contractTitle, signStatus);
        PageInfo<Contract> pageInfo = new PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("total", pageInfo.getTotal());
        result.put("records", pageInfo.getList());
        return result;
    }
    @Override
    public List<Contract> findByLandlordAndTenant(Integer landlordId, Integer tenantId) {
        return contractMapper.findByLandlordAndTenant(landlordId, tenantId);
    }
}