package com.dl.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RentOrderVO {
    private Integer id;
    private Integer houseId;
    private String title;       // 房屋标题
    private String coverImg;    // 房屋封面
    private BigDecimal totalPrice;
    private String orderStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createTime;
    private BigDecimal housePrice; // 房源单价
}
