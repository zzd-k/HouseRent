
package com.dl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RepairProgress {
    private Integer id;
    @NotNull
    private Integer requestId;
    @NotNull
    private Integer operatorId;
    @NotEmpty
    private String action;
    private String details;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}