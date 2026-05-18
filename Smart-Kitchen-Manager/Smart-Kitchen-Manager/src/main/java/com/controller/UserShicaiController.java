package com.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.annotation.IgnoreAuth;
import com.entity.UserShicaiEntity;
import com.service.UserShicaiService;
import com.service.CaipuxinxiService;
import com.service.ExpiryReminderService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 用户食材库
 * 后端接口
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
@RestController
@RequestMapping("/usershicai")
public class UserShicaiController {
    @Autowired
    private UserShicaiService userShicaiService;
    
    @Autowired
    private CaipuxinxiService caipuxinxiService;
    
    @Autowired
    private ExpiryReminderService expiryReminderService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, UserShicaiEntity userShicai,
		HttpServletRequest request){
		// 从session获取tableName，添加空值检查
		Object tableNameObj = request.getSession().getAttribute("tableName");
		if(tableNameObj != null) {
			String tableName = tableNameObj.toString();
			if(tableName.equals("yonghu")) {
				userShicai.setUserid((Long)request.getSession().getAttribute("userId"));
			}
		}
		// 如果参数中有userid，使用参数中的userid（前端传递）
		if(params.get("userid") != null) {
			userShicai.setUserid(Long.parseLong(params.get("userid").toString()));
		}
        EntityWrapper<UserShicaiEntity> ew = new EntityWrapper<UserShicaiEntity>();

		PageUtils page = userShicaiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, userShicai), params), params));
		
		// 自动更新过期食材状态
		userShicaiService.autoUpdateExpiredStatus();

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, UserShicaiEntity userShicai, 
		HttpServletRequest request){
        EntityWrapper<UserShicaiEntity> ew = new EntityWrapper<UserShicaiEntity>();

		PageUtils page = userShicaiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, userShicai), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(UserShicaiEntity userShicai){
       	EntityWrapper<UserShicaiEntity> ew = new EntityWrapper<UserShicaiEntity>();
      	ew.allEq(MPUtil.allEQMapPre(userShicai, "usershicai")); 
        return R.ok().put("data", userShicaiService.selectList(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(UserShicaiEntity userShicai){
        EntityWrapper<UserShicaiEntity> ew = new EntityWrapper<UserShicaiEntity>();
 		ew.allEq(MPUtil.allEQMapPre(userShicai, "usershicai")); 
		UserShicaiEntity userShicaiEntity = userShicaiService.selectOne(ew);
		return R.ok("查询用户食材库成功").put("data", userShicaiEntity);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        UserShicaiEntity userShicai = userShicaiService.selectById(id);
        return R.ok().put("data", userShicai);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        UserShicaiEntity userShicai = userShicaiService.selectById(id);
        return R.ok().put("data", userShicai);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserShicaiEntity userShicai, HttpServletRequest request){
    	userShicai.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	
    	// 从session获取用户信息（如果有）
    	Object tableNameObj = request.getSession().getAttribute("tableName");
    	if(tableNameObj != null) {
    		String tableName = tableNameObj.toString();
    		if(tableName.equals("yonghu")) {
    			userShicai.setUserid((Long)request.getSession().getAttribute("userId"));
    		}
    	}
    	
    	// 如果userid仍然为空，返回错误
    	if(userShicai.getUserid() == null) {
    		return R.error("用户ID不能为空，请先登录");
    	}
		
		userShicai.setAddtime(new Date());
        userShicaiService.insert(userShicai);
        
        // 清除用户推荐缓存
        if (userShicai.getUserid() != null) {
            try {
                caipuxinxiService.clearUserRecommendCache(userShicai.getUserid());
            } catch (Exception e) {
                // 缓存清除失败不影响主流程
            }
        }
        
        return R.ok();
    }
    
    /**
     * 批量保存（用于小票识别）
     */
    @RequestMapping("/batchSave")
    public R batchSave(@RequestBody java.util.List<UserShicaiEntity> userShicaiList, HttpServletRequest request){
        for(UserShicaiEntity userShicai : userShicaiList) {
            userShicai.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
            userShicai.setAddtime(new Date());
            userShicaiService.insert(userShicai);
        }
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody UserShicaiEntity userShicai, HttpServletRequest request){
    	userShicai.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	
		userShicai.setAddtime(new Date());
        userShicaiService.insert(userShicai);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody UserShicaiEntity userShicai, HttpServletRequest request){
        // 获取旧的食材信息
        UserShicaiEntity oldEntity = userShicaiService.selectById(userShicai.getId());
        
        if (oldEntity != null) {
            // 如果状态变为used或discarded，删除待处理提醒
            if (("used".equals(userShicai.getStatus()) || "discarded".equals(userShicai.getStatus()))
                && !"used".equals(oldEntity.getStatus()) && !"discarded".equals(oldEntity.getStatus())) {
                expiryReminderService.deletePendingRemindersByShicai(userShicai.getId());
            }
            
            // 如果过期日期变化，更新提醒日期
            if (userShicai.getExpiryDate() != null 
                && !userShicai.getExpiryDate().equals(oldEntity.getExpiryDate())) {
                expiryReminderService.updateReminderDateByShicai(
                    userShicai.getId(), 
                    userShicai.getExpiryDate()
                );
            }
        }
        
        userShicaiService.updateById(userShicai);
        
        // 清除用户推荐缓存
        if (userShicai.getUserid() != null) {
            try {
                caipuxinxiService.clearUserRecommendCache(userShicai.getUserid());
            } catch (Exception e) {
                // 缓存清除失败不影响主流程
            }
        }
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Transactional
    public R delete(@RequestBody Long[] ids){
        // 获取要删除的食材信息，用于清除缓存
        java.util.Set<Long> userIds = new java.util.HashSet<>();
        for (Long id : ids) {
            UserShicaiEntity entity = userShicaiService.selectById(id);
            if (entity != null && entity.getUserid() != null) {
                userIds.add(entity.getUserid());
            }
            
            // 删除食材前，先删除关联的提醒
            expiryReminderService.delete(
                new EntityWrapper<com.entity.ExpiryReminderEntity>()
                    .eq("user_shicai_id", id)
            );
        }
        
        userShicaiService.deleteBatchIds(Arrays.asList(ids));
        
        // 清除相关用户的推荐缓存
        for (Long userId : userIds) {
            try {
                caipuxinxiService.clearUserRecommendCache(userId);
            } catch (Exception e) {
                // 缓存清除失败不影响主流程
            }
        }
        
        return R.ok();
    }
    
    /**
     * 根据用户ID获取食材库
     */
    @RequestMapping("/listByUser")
    public R listByUser(@RequestParam Long userid){
        EntityWrapper<UserShicaiEntity> ew = new EntityWrapper<UserShicaiEntity>();
        ew.eq("userid", userid);
        return R.ok().put("data", userShicaiService.selectList(ew));
    }
    
    /**
     * 根据状态查询食材
     */
    @RequestMapping("/listByStatus")
    public R listByStatus(@RequestParam Long userid, @RequestParam String status){
        EntityWrapper<UserShicaiEntity> ew = new EntityWrapper<UserShicaiEntity>();
        ew.eq("userid", userid);
        ew.eq("status", status);
        return R.ok().put("data", userShicaiService.selectList(ew));
    }
    
    /**
     * 查询即将过期的食材（3天内）
     */
    @RequestMapping("/listExpiringSoon")
    public R listExpiringSoon(@RequestParam Long userid){
        EntityWrapper<UserShicaiEntity> ew = new EntityWrapper<UserShicaiEntity>();
        ew.eq("userid", userid);
        ew.eq("status", "new");
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        ew.le("expiry_date", calendar.getTime());
        ew.ge("expiry_date", new Date());
        
        return R.ok().put("data", userShicaiService.selectList(ew));
    }

}
