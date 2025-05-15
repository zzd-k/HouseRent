package com.dl.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RentOrder {
    private Integer id;
    private Integer houseId;    // 房源ID
    private Integer tenantId;   // 租客ID
    private LocalDateTime startDate;  // 租赁开始时间
    private LocalDateTime endDate;    // 租赁结束时间
    private BigDecimal totalPrice;    // 总租金
    private String orderStatus;   // 订单状态（待支付、已支付、已取消）
    private LocalDateTime createTime;  // 创建时间
    private LocalDateTime updateTime;  // 更新时间
}
