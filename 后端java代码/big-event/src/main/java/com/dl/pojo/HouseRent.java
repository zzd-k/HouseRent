package com.dl.pojo;

import com.dl.anno.Rent;

import com.dl.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HouseRent {
    @NotNull(groups = update.class)
    private Integer id;//主键ID
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;//文章标题
    @NotEmpty
    private String content;//文章内容
    @URL
    @NotEmpty
    private String coverImg;//封面图像
    @Rent
    private String state;//发布状态 在售|已售空
    @NotNull
    private Integer houserentId;//文章分类id
    private Integer createUser;//创建人ID
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
    private BigDecimal price;       // 租金
    private Double area;            // 房屋面积
    private String location;        // 地理位置
    private String tags;       // 标签（逗号分隔）
    private String region;     // 区域代码
//    private String layoutType; // 户型
//    @NotEmpty
    private String type;// 房屋类型（如 公寓/别墅/民宅）

    private String floor;// 楼层信息（如 中层/高层）

    private String decoration;// 装修情况（如 精装/简装）
    // 租赁方式（如 整租/合租）
    @Pattern(regexp = "整租|合租", message = "租赁方式必须是整租或合租")
    private String rentType;
    // 是否包含物业费
    private Boolean includePropertyFee;
    // 配套设施（如 空调/WiFi）
    private String facilities;

    private String auditStatus; // 审核状态：待审核、审核通过、审核不通过

    public Integer getLandlordId() {
        return this.createUser; // 房东ID就是创建用户ID
    }

    public String getRentStatus() {
        return this.state; // 租赁状态就是state字段
    }

    public interface update extends Default {

    }
}
