package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.DietStatisticsDao;
import com.entity.DietStatisticsEntity;
import com.service.DietStatisticsService;

@Service("dietStatisticsService")
public class DietStatisticsServiceImpl extends ServiceImpl<DietStatisticsDao, DietStatisticsEntity> implements DietStatisticsService {
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DietStatisticsEntity> page = this.selectPage(
                new Query<DietStatisticsEntity>(params).getPage(),
                new EntityWrapper<DietStatisticsEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DietStatisticsEntity> wrapper) {
		Page<DietStatisticsEntity> page = this.selectPage(
                new Query<DietStatisticsEntity>(params).getPage(),
                wrapper
        );
	    return new PageUtils(page);
 	}

}
