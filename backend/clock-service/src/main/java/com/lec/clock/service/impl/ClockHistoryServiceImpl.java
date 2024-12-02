package com.lec.clock.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lec.clock.entity.pojo.ClockHistory;
import com.lec.clock.mapper.ClockHistoryMapper;
import com.lec.clock.service.ClockHistoryService;
import com.lec.clock.utils.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Slf4j
@Service("clockHistoryService")
public class ClockHistoryServiceImpl extends ServiceImpl<ClockHistoryMapper, ClockHistory> implements ClockHistoryService {

    @Autowired
    ClockHistoryMapper mapper;

    @Override
    public PageResult list(Integer pageNum, Integer pageSize) {
        log.info("搜索打卡未满的记录");
        PageHelper.startPage(pageNum, pageSize);
        Page<ClockHistory> page = mapper.getAllClock();
        log.info("查询到的数量有：{}", page.getTotal());
        return new PageResult(page.getTotal(), page.getResult());
    }

}

