package com.lec.clock.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * (Clock)表实体类
 *
 * @author makejava
 * @since 2023-03-14 16:02:03
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clock {

    @TableId
    private Long id;
    //开始时间
    private LocalDateTime beginTime;

    //0未打卡,1正在打卡
    private Integer status;

    //总共打卡的时间
    private Integer totalDuration;

    //需要打卡时间
    private Integer targetDuration;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 例如使用加时卡，因为加时卡是暂时加一小时，所以先让目标时长+60（分钟）,然后让temporary-60（分钟）,然后等一周开始的时候
     * 使用定时任务让 targetDuration=targetDuration+temporary，这样就实现了一周之内暂时增加一小时（同理减时卡）
     */
    private Integer temporary;

    public Clock(LocalDateTime beginTime, Integer status, Integer totalDuration, Integer targetDuration) {
        this.beginTime = beginTime;
        this.status = status;
        this.totalDuration = totalDuration;
        this.targetDuration = targetDuration;
    }
}

