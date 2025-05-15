// ContractHistory.java
package com.dl.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContractHistory {
    private Integer id;
    private Integer contractId;
    private String action;
    private String description;
    private Integer changedBy;
    private LocalDateTime changedAt;
}