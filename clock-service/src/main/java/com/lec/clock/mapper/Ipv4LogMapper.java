package com.lec.clock.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lec.clock.entity.pojo.Ipv4Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface Ipv4LogMapper extends BaseMapper<Ipv4Log> {

    @Insert("insert into ipv4_log(ipv4) values (#{ipv4})")
    public void insert(@Param("ipv4") String ipv4);

    @Select("select * from ipv4_log where ipv4=#{ipv4}")
    public String select(@Param("ipv4") String ipv4);
}
