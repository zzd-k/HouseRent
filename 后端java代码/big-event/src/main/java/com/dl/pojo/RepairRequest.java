// RepairRequest.java
package com.dl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RepairRequest {
    private Integer id;
    @NotNull
    private Integer houseId;

    private Integer userId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    private String images;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}