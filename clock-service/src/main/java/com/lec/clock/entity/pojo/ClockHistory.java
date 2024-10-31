package com.lec.clock.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (ClockHistory)表实体类
 *
 * @author makejava
 * @since 2023-03-14 16:03:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockHistory {
    
    private Long id;

    //周数
    private Integer week;
    //时长
    private Integer duration;
    //0未达标,1达标
    private Integer isStandard;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    public ClockHistory(Long id, Integer week, Integer duration, Integer isStandard) {
        this.id = id;
        this.week = week;
        this.duration = duration;
        this.isStandard = isStandard;
    }
}

