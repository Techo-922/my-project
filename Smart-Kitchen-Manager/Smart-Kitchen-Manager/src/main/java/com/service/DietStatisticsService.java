package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.DietStatisticsEntity;
import java.util.List;
import java.util.Map;

/**
 * 饮食统计
 *
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
public interface DietStatisticsService extends IService<DietStatisticsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	PageUtils queryPage(Map<String, Object> params, Wrapper<DietStatisticsEntity> wrapper);
}
