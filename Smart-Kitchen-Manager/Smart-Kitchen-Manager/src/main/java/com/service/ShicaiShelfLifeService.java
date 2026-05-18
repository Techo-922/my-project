package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ShicaiShelfLifeEntity;
import java.util.List;
import java.util.Map;

/**
 * 保质期参考库
 *
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
public interface ShicaiShelfLifeService extends IService<ShicaiShelfLifeEntity> {

    /**
     * 分页查询保质期参考库
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 分页查询保质期参考库（带自定义条件）
     */
   	PageUtils queryPage(Map<String, Object> params, Wrapper<ShicaiShelfLifeEntity> wrapper);
   	
   	/**
     * 根据食材ID查询保质期信息
     * 
     * @param shicaiId 食材ID
     * @return 保质期信息
     */
    ShicaiShelfLifeEntity getByShicaiId(Long shicaiId);
    
    /**
     * 添加保质期参考（带验证）
     * 
     * @param entity 保质期实体
     * @return 是否成功
     */
    boolean saveWithValidation(ShicaiShelfLifeEntity entity);
    
    /**
     * 更新保质期参考（带验证）
     * 
     * @param entity 保质期实体
     * @return 是否成功
     */
    boolean updateWithValidation(ShicaiShelfLifeEntity entity);
    
    /**
     * 验证保质期数据
     * 
     * @param entity 保质期实体
     * @return 是否验证通过
     */
    boolean validateShelfLife(ShicaiShelfLifeEntity entity);
}
