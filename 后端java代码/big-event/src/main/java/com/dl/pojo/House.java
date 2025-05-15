package com.dl.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// House.java
@Data
@Entity
@Table(name = "house")
public class House {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;       // 房源标题
    private String description; // 房源描述
    private BigDecimal price;   // 月租金

    @Column(name = "area_size")
    private Double areaSize;    // 面积

    private String address;     // 详细地址
    private String city;        // 所在城市

    @Column(name = "room_type")
    private String roomType;    // 户型（如：三室一厅）

    @Column(name = "main_image")
    private String mainImage;   // 主图URL

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    // 其他字段...
}