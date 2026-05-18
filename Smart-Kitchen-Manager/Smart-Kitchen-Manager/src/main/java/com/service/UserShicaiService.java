package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.UserShicaiEntity;
import java.util.List;
import java.util.Map;

/**
 * 用户食材库
 *
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
public interface UserShicaiService extends IService<UserShicaiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	PageUtils queryPage(Map<String, Object> params, Wrapper<UserShicaiEntity> wrapper);
   	
   	/**
   	 * 自动更新过期食材状态
   	 * 将过期日期小于当前时间且状态为new的食材更新为expired
   	 */
   	void autoUpdateExpiredStatus();
}
