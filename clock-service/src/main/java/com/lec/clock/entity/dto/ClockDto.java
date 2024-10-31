package com.lec.clock.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockDto {

    //子网掩码
    private String subnetMask;
    //IP地址
    private String ipAddress;
}
