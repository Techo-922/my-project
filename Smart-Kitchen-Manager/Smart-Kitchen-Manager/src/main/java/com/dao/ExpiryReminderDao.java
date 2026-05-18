package com.dao;

import com.entity.ExpiryReminderEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;

/**
 * 过期提醒
 * 
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
public interface ExpiryReminderDao extends BaseMapper<ExpiryReminderEntity> {
	
	/**
     * 查询提醒详情（包含关联的食材信息）
     * @param id 提醒ID
     * @return 包含提醒和食材信息的Map
     */
    Map<String, Object> selectReminderDetail(@Param("id") Long id);
    
    /**
     * 查询未读提醒数量
     * @param userid 用户ID
     * @return 未读提醒数量
     */
    int selectUnreadCount(@Param("userid") Long userid);
}
