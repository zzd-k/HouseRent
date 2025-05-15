package com.dl.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Contract {
    private Integer id;
    private String contractTitle;
    private Integer houseId;
    private Integer tenantId;
    private Integer landlordId;
    private String content; // 富文本 HTML
    private String signStatus; // 未签署/已签署
    private LocalDateTime signedTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 非数据库字段，用于展示
    private String houseName;  // 房源标题
    private String tenantName; // 租客用户名
    private Boolean landlordSigned;
    private Boolean tenantSigned;


}