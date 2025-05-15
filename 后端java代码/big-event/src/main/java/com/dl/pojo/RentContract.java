package com.dl.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RentContract {
    @NotNull(groups = Update.class)
    private Integer id; // 合同ID

    @NotNull
    private Integer houseId; // 关联的房屋ID

    @NotNull
    private Integer landlordId; // 房东ID

    @NotNull
    private Integer tenantId; // 租客ID

    @NotEmpty
    private String contractNumber; // 合同编号

    @NotNull
    private LocalDateTime startDate; // 租赁开始日期

    @NotNull
    private LocalDateTime endDate; // 租赁结束日期

    @NotNull
    private BigDecimal rentAmount; // 租金金额

    @NotEmpty
    private String paymentMethod; // 支付方式

    private String deposit; // 押金信息

    @NotEmpty
    private String terms; // 合同条款

    private String status; // 合同状态(生效中/已终止/已过期)

    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    public interface Update {}
}