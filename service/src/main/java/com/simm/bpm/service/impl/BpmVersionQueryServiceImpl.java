package com.simm.bpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.simm.bpm.entity.BpmVersion;
import com.simm.bpm.mapper.BpmVersionMapper;
import com.simm.bpm.service.IBpmVersionQueryService;
import lombok.AllArgsConstructor;
import org.apache.ibatis.reflection.wrapper.BaseWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author miscr
 */
@Service
@AllArgsConstructor
public class BpmVersionQueryServiceImpl implements IBpmVersionQueryService {
    final BpmVersionMapper bpmVersionMapper;

    @Override
    public List<BpmVersion> getAll() {
        QueryWrapper<BpmVersion> wrapper = new QueryWrapper();
        wrapper.lambda().eq(BpmVersion::getName, "4.0.11");
        return bpmVersionMapper.selectList(wrapper);
    }
}
