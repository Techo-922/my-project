package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ExpiryReminderEntity;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 过期提醒
 *
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
public interface ExpiryReminderService extends IService<ExpiryReminderEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	PageUtils queryPage(Map<String, Object> params, Wrapper<ExpiryReminderEntity> wrapper);
   	
   	/**
     * 查询提醒详情（包含关联的食材信息）
     * @param id 提醒ID
     * @return 包含提醒和食材信息的Map
     */
    Map<String, Object> getReminderDetail(Long id);
    
    /**
     * 标记提醒为已读
     * @param id 提醒ID
     * @param userid 用户ID（用于权限验证）
     * @return 是否成功
     */
    boolean markAsRead(Long id, Long userid);
    
    /**
     * 查询未读提醒数量
     * @param userid 用户ID
     * @return 未读提醒数量
     */
    int getUnreadCount(Long userid);
    
    /**
     * 为指定食材创建提醒
     * @param userid 用户ID
     * @param userShicaiId 用户食材ID
     * @param remindDate 提醒日期
     * @return 是否成功
     */
    boolean createReminder(Long userid, Long userShicaiId, Date remindDate);
    
    /**
     * 删除指定食材的所有待处理提醒
     * @param userShicaiId 用户食材ID
     */
    void deletePendingRemindersByShicai(Long userShicaiId);
    
    /**
     * 更新食材关联的提醒日期
     * @param userShicaiId 用户食材ID
     * @param newExpiryDate 新的过期日期
     */
    void updateReminderDateByShicai(Long userShicaiId, Date newExpiryDate);
}
