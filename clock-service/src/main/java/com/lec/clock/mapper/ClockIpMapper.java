package com.lec.clock.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClockIpMapper {

    @Select("select * from clock_ip where ip = #{ip}")
    public String getClockIp(@Param("ip") String ip);

    @Insert("insert into clock_ip(ip) values (#{ip})")
    public void insertClockIp(@Param("ip") String ip);

    @Select("select * from clock_ip")
    public List<String> getClockIpList();
}
