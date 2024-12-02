package com.lec.clock.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lec.clock.entity.pojo.Ipv4Log;
import com.lec.clock.mapper.Ipv4LogMapper;
import com.lec.clock.service.Ipv4LogService;
import org.springframework.stereotype.Service;

@Service("Ipv4LogService")
public class Ipv4LogServiceImpl extends ServiceImpl<Ipv4LogMapper, Ipv4Log> implements Ipv4LogService {
}
