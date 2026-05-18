package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.ShicaiShelfLifeDao;
import com.entity.ShicaiShelfLifeEntity;
import com.service.ShicaiShelfLifeService;

/**
 * 保质期参考库服务实现
 * 
 * @author 
 * @date 2023-04-25
 */
@Service("shicaiShelfLifeService")
public class ShicaiShelfLifeServiceImpl extends ServiceImpl<ShicaiShelfLifeDao, ShicaiShelfLifeEntity> implements ShicaiShelfLifeService {
	
	private static final Logger logger = LoggerFactory.getLogger(ShicaiShelfLifeServiceImpl.class);
	
	@Autowired
	private ShicaiShelfLifeDao shicaiShelfLifeDao;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ShicaiShelfLifeEntity> page = this.selectPage(
                new Query<ShicaiShelfLifeEntity>(params).getPage(),
                new EntityWrapper<ShicaiShelfLifeEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<ShicaiShelfLifeEntity> wrapper) {
		Page<ShicaiShelfLifeEntity> page = this.selectPage(
                new Query<ShicaiShelfLifeEntity>(params).getPage(),
                wrapper
        );
        return new PageUtils(page);
 	}
 	
 	@Override
    public ShicaiShelfLifeEntity getByShicaiId(Long shicaiId) {
        if (shicaiId == null) {
            logger.warn("根据食材ID查询保质期失败 - 食材ID为空");
            return null;
        }
        
        ShicaiShelfLifeEntity entity = shicaiShelfLifeDao.selectByShicaiId(shicaiId);
        
        if (entity == null) {
            logger.debug("食材ID{}没有保质期参考信息", shicaiId);
        } else {
            logger.debug("查询到食材ID{}的保质期信息: {}天", shicaiId, entity.getShelfLifeDays());
        }
        
        return entity;
    }
    
    @Override
    @Transactional
    public boolean saveWithValidation(ShicaiShelfLifeEntity entity) {
        // 数据验证
        if (!validateShelfLife(entity)) {
            return false;
        }
        
        // 检查是否已存在相同食材ID的记录
        ShicaiShelfLifeEntity existing = this.selectOne(
            new EntityWrapper<ShicaiShelfLifeEntity>()
                .eq("shicai_id", entity.getShicaiId())
        );
        
        boolean result;
        
        if (existing != null) {
            // 如果已存在，则更新现有记录
            logger.info("食材ID{}的保质期参考已存在，将更新现有记录", entity.getShicaiId());
            entity.setId(existing.getId());
            entity.setAddtime(existing.getAddtime()); // 保留原添加时间
            result = this.updateById(entity);
            
            if (result) {
                logger.info("更新保质期参考成功 - 食材ID: {}, 保质期: {}天, 存储方式: {}", 
                    entity.getShicaiId(), entity.getShelfLifeDays(), entity.getStorageMethod());
            } else {
                logger.error("更新保质期参考失败 - 食材ID: {}", entity.getShicaiId());
            }
        } else {
            // 如果不存在，则新增记录
            entity.setAddtime(new Date());
            result = this.insert(entity);
            
            if (result) {
                logger.info("保存保质期参考成功 - 食材ID: {}, 保质期: {}天, 存储方式: {}", 
                    entity.getShicaiId(), entity.getShelfLifeDays(), entity.getStorageMethod());
            } else {
                logger.error("保存保质期参考失败 - 食材ID: {}", entity.getShicaiId());
            }
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public boolean updateWithValidation(ShicaiShelfLifeEntity entity) {
        if (entity.getId() == null) {
            logger.warn("更新保质期参考失败 - ID为空");
            return false;
        }
        
        // 检查记录是否存在
        ShicaiShelfLifeEntity existing = this.selectById(entity.getId());
        if (existing == null) {
            logger.warn("更新保质期参考失败 - 记录不存在, ID: {}", entity.getId());
            return false;
        }
        
        // 数据验证（只验证非空字段）
        if (entity.getShelfLifeDays() != null && entity.getShelfLifeDays() <= 0) {
            logger.error("更新保质期参考失败 - 保质期天数必须大于0");
            return false;
        }
        
        if (entity.getStorageMethod() != null && StringUtils.isBlank(entity.getStorageMethod())) {
            logger.error("更新保质期参考失败 - 存储方式不能为空");
            return false;
        }
        
        // 如果更新了食材ID，需要检查唯一性
        if (entity.getShicaiId() != null && !entity.getShicaiId().equals(existing.getShicaiId())) {
            ShicaiShelfLifeEntity duplicate = this.selectOne(
                new EntityWrapper<ShicaiShelfLifeEntity>()
                    .eq("shicai_id", entity.getShicaiId())
                    .ne("id", entity.getId())
            );
            
            if (duplicate != null) {
                logger.warn("更新保质期参考失败 - 食材ID{}的保质期参考已存在", entity.getShicaiId());
                return false;
            }
        }
        
        boolean result = this.updateById(entity);
        
        if (result) {
            logger.info("更新保质期参考成功 - ID: {}", entity.getId());
        } else {
            logger.error("更新保质期参考失败 - ID: {}", entity.getId());
        }
        
        return result;
    }
    
    @Override
    public boolean validateShelfLife(ShicaiShelfLifeEntity entity) {
        if (entity == null) {
            logger.error("保质期数据验证失败 - 实体对象为空");
            return false;
        }
        
        // 验证食材ID不能为空
        if (entity.getShicaiId() == null) {
            logger.error("保质期数据验证失败 - 食材ID不能为空");
            return false;
        }
        
        // 验证保质期天数不能为空且必须大于0
        if (entity.getShelfLifeDays() == null) {
            logger.error("保质期数据验证失败 - 保质期天数不能为空");
            return false;
        }
        
        if (entity.getShelfLifeDays() <= 0) {
            logger.error("保质期数据验证失败 - 保质期天数必须大于0，当前值: {}", entity.getShelfLifeDays());
            return false;
        }
        
        // 验证存储方式不能为空
        if (StringUtils.isBlank(entity.getStorageMethod())) {
            logger.error("保质期数据验证失败 - 存储方式不能为空");
            return false;
        }
        
        logger.debug("保质期数据验证通过 - 食材ID: {}, 保质期: {}天, 存储方式: {}", 
            entity.getShicaiId(), entity.getShelfLifeDays(), entity.getStorageMethod());
        
        return true;
    }
}
