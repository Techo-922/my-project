package com.dao;

import com.entity.ShicaiShelfLifeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 食材保质期参考库
 * 
 * @author 
 * @date 2023-04-25
 */
public interface ShicaiShelfLifeDao extends BaseMapper<ShicaiShelfLifeEntity> {
    
    /**
     * 根据食材ID查询保质期信息
     * 
     * @param shicaiId 食材ID
     * @return 保质期信息
     */
    ShicaiShelfLifeEntity selectByShicaiId(@Param("shicaiId") Long shicaiId);
}
