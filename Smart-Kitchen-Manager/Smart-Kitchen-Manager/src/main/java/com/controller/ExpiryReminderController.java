package com.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ExpiryReminderEntity;
import com.entity.UserShicaiEntity;
import com.scheduled.ExpiryReminderScheduledTask;
import com.service.ExpiryReminderService;
import com.service.UserShicaiService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 过期提醒
 * 后端接口
 * @author 
 * @email 
 * @date 2024-12-30
 */
@RestController
@RequestMapping("/expiryreminder")
public class ExpiryReminderController {
    
    @Autowired
    private ExpiryReminderService expiryReminderService;
    
    @Autowired
    private ExpiryReminderScheduledTask expiryReminderScheduledTask;
    
    @Autowired
    private UserShicaiService userShicaiService;

    /**
     * 分页查询提醒列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ExpiryReminderEntity expiryReminder,
                  HttpServletRequest request) {
        // 获取当前登录用户ID
        Object tableNameObj = request.getSession().getAttribute("tableName");
        if (tableNameObj != null) {
            String tableName = tableNameObj.toString();
            if (tableName.equals("yonghu")) {
                expiryReminder.setUserid((Long) request.getSession().getAttribute("userId"));
            }
        }
        
        // 如果参数中有userid，使用参数中的userid（前端传递）
        if (params.get("userid") != null) {
            expiryReminder.setUserid(Long.parseLong(params.get("userid").toString()));
        }
        
        // 构建查询条件
        EntityWrapper<ExpiryReminderEntity> ew = new EntityWrapper<ExpiryReminderEntity>();
        
        // 按提醒日期倒序排列
        ew.orderBy("remind_date", false);
        
        PageUtils page = expiryReminderService.queryPage(params, 
            MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, expiryReminder), params), params));
        
        // 补充食材名称信息
        List<ExpiryReminderEntity> list = (List<ExpiryReminderEntity>) page.getList();
        List<Map<String, Object>> enrichedList = new java.util.ArrayList<>();
        
        for (ExpiryReminderEntity reminder : list) {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", reminder.getId());
            map.put("userid", reminder.getUserid());
            map.put("userShicaiId", reminder.getUserShicaiId());
            map.put("remindDate", reminder.getRemindDate());
            map.put("status", reminder.getStatus());
            map.put("addtime", reminder.getAddtime());
            
            // 查询关联的食材信息
            if (reminder.getUserShicaiId() != null) {
                UserShicaiEntity shicai = userShicaiService.selectById(reminder.getUserShicaiId());
                if (shicai != null) {
                    map.put("shicaiName", shicai.getShicaiName());
                    map.put("expiryDate", shicai.getExpiryDate());
                }
            }
            
            enrichedList.add(map);
        }
        
        page.setList(enrichedList);

        return R.ok().put("data", page);
    }

    /**
     * 查询提醒详情（包含关联的食材信息）
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        // 查询提醒详情
        Map<String, Object> reminderDetail = expiryReminderService.getReminderDetail(id);
        
        if (reminderDetail == null) {
            return R.error("提醒不存在");
        }
        
        // 权限验证：确保提醒属于当前用户
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            Long currentUserId = (Long) request.getSession().getAttribute("userId");
            Long reminderUserId = (Long) reminderDetail.get("userid");
            
            if (!currentUserId.equals(reminderUserId)) {
                return R.error(403, "无权访问该提醒");
            }
        }
        
        return R.ok().put("data", reminderDetail);
    }

    /**
     * 标记提醒为已读
     */
    @RequestMapping("/markAsRead")
    public R markAsRead(@RequestParam Long id, @RequestParam(required = false) Long userid, HttpServletRequest request) {
        // 如果前端没有传userid，从session获取
        if (userid == null) {
            Object tableNameObj = request.getSession().getAttribute("tableName");
            if (tableNameObj != null) {
                String tableName = tableNameObj.toString();
                if (tableName.equals("yonghu")) {
                    userid = (Long) request.getSession().getAttribute("userId");
                }
            }
        }
        
        if (userid == null) {
            return R.error("用户未登录");
        }
        
        // 调用服务层方法，包含权限验证
        boolean success = expiryReminderService.markAsRead(id, userid);
        
        if (success) {
            return R.ok("标记成功");
        } else {
            return R.error("标记失败，提醒不存在或无权操作");
        }
    }

    /**
     * 删除提醒
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request) {
        // 获取当前用户ID
        Long userid = null;
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            userid = (Long) request.getSession().getAttribute("userId");
        }
        
        if (userid == null) {
            return R.error("用户未登录");
        }
        
        // 权限验证：确保所有提醒都属于当前用户
        for (Long id : ids) {
            ExpiryReminderEntity reminder = expiryReminderService.selectById(id);
            if (reminder == null) {
                return R.error("提醒ID " + id + " 不存在");
            }
            if (!reminder.getUserid().equals(userid)) {
                return R.error(403, "无权删除该提醒");
            }
        }
        
        // 执行删除
        expiryReminderService.deleteBatchIds(Arrays.asList(ids));
        
        return R.ok("删除成功");
    }

    /**
     * 查询未读提醒数量
     */
    @RequestMapping("/unreadCount")
    public R unreadCount(HttpServletRequest request) {
        // 获取当前用户ID
        Long userid = null;
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            userid = (Long) request.getSession().getAttribute("userId");
        }
        
        if (userid == null) {
            return R.error("用户未登录");
        }
        
        // 查询未读数量
        int count = expiryReminderService.getUnreadCount(userid);
        
        return R.ok().put("count", count);
    }

    /**
     * 手动触发定时任务（测试用）
     */
    @RequestMapping("/triggerCheck")
    public R triggerCheck() {
        try {
            expiryReminderScheduledTask.manualTrigger();
            return R.ok("定时任务触发成功");
        } catch (Exception e) {
            return R.error("定时任务触发失败：" + e.getMessage());
        }
    }
}
