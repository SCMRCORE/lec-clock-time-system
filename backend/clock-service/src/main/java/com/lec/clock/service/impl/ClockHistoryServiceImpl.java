package com.lec.clock.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lec.clock.entity.pojo.ClockHistory;
import com.lec.clock.entity.vo.ClockHistoryVo;
import com.lec.clock.mapper.ClockHistoryMapper;
import com.lec.clock.service.ClockHistoryService;
import com.lec.clock.utils.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("clockHistoryService")
public class ClockHistoryServiceImpl extends ServiceImpl<ClockHistoryMapper, ClockHistory> implements ClockHistoryService {

    @Autowired
    ClockHistoryMapper mapper;

    @Override
    public PageResult list(Integer pageNum, Integer pageSize) {
        //TODO ClockHistory有很多冗余数据需要清除
        log.info("service层:搜索打卡未满的记录");
        PageHelper.startPage(pageNum, pageSize);
        Page<ClockHistoryVo> page = mapper.getAllClock();
        log.info("service层:查询到的数量有：{}", page.size());
        return new PageResult(page.size(), page.getResult());
    }

}

