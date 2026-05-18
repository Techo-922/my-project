package com.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.UserShicaiDao;
import com.entity.UserShicaiEntity;
import com.service.UserShicaiService;

@Service("userShicaiService")
public class UserShicaiServiceImpl extends ServiceImpl<UserShicaiDao, UserShicaiEntity> implements UserShicaiService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserShicaiServiceImpl.class);
	
	@Autowired
	private UserShicaiDao userShicaiDao;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<UserShicaiEntity> page = this.selectPage(
                new Query<UserShicaiEntity>(params).getPage(),
                new EntityWrapper<UserShicaiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<UserShicaiEntity> wrapper) {
		Page<UserShicaiEntity> page = this.selectPage(
                new Query<UserShicaiEntity>(params).getPage(),
                wrapper
        );
	    return new PageUtils(page);
 	}
 	
 	@Override
 	public void autoUpdateExpiredStatus() {
 		try {
 			// 查询所有过期但状态仍为new的食材
 			EntityWrapper<UserShicaiEntity> ew = new EntityWrapper<UserShicaiEntity>();
 			ew.eq("status", "new");
 			ew.lt("expiry_date", new Date());  // 过期日期小于当前时间
 			
 			List<UserShicaiEntity> expiredList = this.selectList(ew);
 			
 			if (expiredList != null && !expiredList.isEmpty()) {
 				logger.info("发现{}个过期食材需要更新状态", expiredList.size());
 				
 				// 批量更新状态为expired
 				for (UserShicaiEntity entity : expiredList) {
 					entity.setStatus("expired");
 					this.updateById(entity);
 				}
 				
 				logger.info("已自动更新{}个过期食材的状态为expired", expiredList.size());
 			}
 		} catch (Exception e) {
 			logger.error("自动更新过期食材状态失败", e);
 		}
 	}

}
