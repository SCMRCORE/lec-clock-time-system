package com.lec.clock.service.impl;


import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clockcommon.entity.PageVo;
import com.clockcommon.entity.Result;
import com.clockcommon.utils.BeanCopyUtils;
import com.lec.clock.entity.pojo.ClockHistory;
import com.lec.clock.entity.vo.ClockHistoryListVo;
import com.lec.clock.entity.vo.ClockHistoryVo;
import com.lec.clock.mapper.ClockHistoryMapper;
import com.lec.clock.service.ClockHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lec.clock.entity.vo.ClockInfoVo;


import javax.annotation.Resource;
import java.util.List;

@Service("clockHistoryService")
public class ClockHistoryServiceImpl extends ServiceImpl<ClockHistoryMapper, ClockHistory> implements ClockHistoryService {

    @Autowired
    ClockHistoryMapper mapper;

    @Override
    public Result list(Integer week, Integer grade, Integer pageNum, Integer pageSize) {
        List<ClockHistoryListVo> clockHistoryListVos = mapper.selectClockHistoryList(week, grade, (pageNum - 1) * pageSize, pageSize);
        return Result.okResult(new PageVo(clockHistoryListVos,clockHistoryListVos.size()));
    }

    @Override
    public Result getClockHistoryById(Long id, Integer pageNum, Integer pageSize) {
        Page<ClockHistory> page = new Page(pageNum, pageSize);
        LambdaQueryWrapper<ClockHistory> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ClockHistory::getId, id).orderByDesc(ClockHistory::getWeek);

        page = page(page, lqw);
        List<ClockHistoryVo> clockHistoryVos = BeanCopyUtils.copyBeanList(page.getRecords(), ClockHistoryVo.class);

        return Result.okResult(new PageVo(clockHistoryVos, page.getTotal()));
    }
}

