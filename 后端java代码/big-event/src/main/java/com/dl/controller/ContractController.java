package com.dl.controller;

import com.dl.mapper.ContractMapper;
import com.dl.pojo.Contract;
import com.dl.pojo.Result;
import com.dl.service.ContractService;
import com.dl.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractMapper contractMapper;

    @PostMapping
    public Result add(@RequestBody Contract contract) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
//        contract.setTenantId(userId); // 默认当前用户是租客
//        System.out.println("🏷️ tenantId = " + contract.getTenantId());
//        System.out.println("🏷️ houseId = " + contract.getHouseId());
        contractService.add(contract);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Contract> findById(@PathVariable Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        if (userId == null) {
            return Result.error("合同不存在");
        }
        return Result.success(contractService.findById(id));
    }

    @GetMapping("/tenant")
    public Result<List<Contract>> findByTenantId() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return Result.success(contractService.findByTenantId(userId));
    }

    @GetMapping("/house/{houseId}")
    public Result<List<Contract>> findByHouseId(@PathVariable Integer houseId) {
        return Result.success(contractService.findByHouseId(houseId));
    }

    @PutMapping("/sign/{id}")
    public Result sign(@PathVariable Integer id, @RequestParam String role) {
        contractService.signContract(id, role);
        return Result.success("签署成功");
    }


    @GetMapping("/list")
    public Result<Map<String, Object>> findByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String contractTitle,
            @RequestParam(required = false) String signStatus
    ) {

        return Result.success(contractService.findByPage(page, pageSize, contractTitle, signStatus));
    }

    @GetMapping("/landlord")
    public Result<List<Contract>> findByLandlordAndOptionalTenant(
            @RequestParam Integer landlordId,
            @RequestParam(required = false) Integer tenantId) {
        if (tenantId != null) {
            return Result.success(contractService.findByLandlordAndTenant(landlordId, tenantId));
        } else {
            return Result.success(contractMapper.findByLandlordId(landlordId));
        }
    }
}