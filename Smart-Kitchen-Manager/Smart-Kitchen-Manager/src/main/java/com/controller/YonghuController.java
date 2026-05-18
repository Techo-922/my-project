package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.YonghuEntity;
import com.entity.view.YonghuView;

import com.service.YonghuService;
import com.service.CaipuxinxiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 用户
 * 后端接口
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
@RestController
@RequestMapping("/yonghu")
public class YonghuController {
    @Autowired
    private YonghuService yonghuService;

    @Autowired
    private CaipuxinxiService caipuxinxiService;
    
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		YonghuEntity u = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", username));
		if(u==null || !u.getMima().equals(password)) {
			return R.error("账号或密码不正确");
		}
		
		String token = tokenService.generateToken(u.getId(), username,"yonghu",  "用户" );
		return R.ok().put("token", token);
	}

	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody YonghuEntity yonghu){
    	//ValidatorUtils.validateEntity(yonghu);
    	YonghuEntity u = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", yonghu.getZhanghao()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		yonghu.setId(uId);
        yonghuService.insert(yonghu);
        return R.ok();
    }

	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        YonghuEntity u = yonghuService.selectById(id);
        return R.ok().put("data", u);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	YonghuEntity u = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setMima("123456");
        yonghuService.updateById(u);
        return R.ok("密码已重置为：123456");
    }


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,YonghuEntity yonghu,
		HttpServletRequest request){
        EntityWrapper<YonghuEntity> ew = new EntityWrapper<YonghuEntity>();

		PageUtils page = yonghuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yonghu), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,YonghuEntity yonghu, 
		HttpServletRequest request){
        EntityWrapper<YonghuEntity> ew = new EntityWrapper<YonghuEntity>();

		PageUtils page = yonghuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yonghu), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( YonghuEntity yonghu){
       	EntityWrapper<YonghuEntity> ew = new EntityWrapper<YonghuEntity>();
      	ew.allEq(MPUtil.allEQMapPre( yonghu, "yonghu")); 
        return R.ok().put("data", yonghuService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(YonghuEntity yonghu){
        EntityWrapper< YonghuEntity> ew = new EntityWrapper< YonghuEntity>();
 		ew.allEq(MPUtil.allEQMapPre( yonghu, "yonghu")); 
		YonghuView yonghuView =  yonghuService.selectView(ew);
		return R.ok("查询用户成功").put("data", yonghuView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        YonghuEntity yonghu = yonghuService.selectById(id);
        return R.ok().put("data", yonghu);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        YonghuEntity yonghu = yonghuService.selectById(id);
        return R.ok().put("data", yonghu);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody YonghuEntity yonghu, HttpServletRequest request){
    	yonghu.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(yonghu);
    	YonghuEntity u = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", yonghu.getZhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		yonghu.setId(new Date().getTime());
        yonghuService.insert(yonghu);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody YonghuEntity yonghu, HttpServletRequest request){
    	yonghu.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(yonghu);
    	YonghuEntity u = yonghuService.selectOne(new EntityWrapper<YonghuEntity>().eq("zhanghao", yonghu.getZhanghao()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		yonghu.setId(new Date().getTime());
        yonghuService.insert(yonghu);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody YonghuEntity yonghu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(yonghu);
        yonghuService.updateById(yonghu);//全部更新
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        yonghuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 获取用户偏好设置
     * GET /yonghu/preference/{userId}
     * 
     * @param userId 用户ID
     * @return 用户偏好设置（healthPreference和allergyInfo）
     */
    @RequestMapping("/preference/{userId}")
    public R getPreference(@PathVariable("userId") Long userId) {
        try {
            if (userId == null) {
                return R.error(400, "用户ID不能为空");
            }
            
            com.dto.UserPreferenceDTO preference = yonghuService.getUserPreference(userId);
            
            if (preference == null) {
                return R.error(404, "用户不存在");
            }
            
            return R.ok().put("data", preference);
            
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(500, "系统异常，请稍后重试");
        }
    }
    
    /**
     * 更新用户偏好设置
     * PUT /yonghu/preference/update
     * 
     * @param request 更新请求（包含userId、healthPreference、allergyInfo）
     * @param httpRequest HTTP请求对象
     * @return 更新结果
     */
    @RequestMapping("/preference/update")
    public R updatePreference(@RequestBody com.dto.PreferenceUpdateRequest request, HttpServletRequest httpRequest) {
        try {
            // 参数校验
            if (request.getUserId() == null) {
                return R.error(400, "用户ID不能为空");
            }
            
            // 权限校验：仅允许修改自身偏好
            Long sessionUserId = (Long) httpRequest.getSession().getAttribute("userId");
            if (sessionUserId != null && !sessionUserId.equals(request.getUserId())) {
                return R.error(403, "无权修改其他用户的偏好设置");
            }
            
            // 执行更新
            boolean result = yonghuService.updateUserPreference(request);
            
            if (result) {
                // 清除用户推荐缓存
                try {
                    caipuxinxiService.clearUserRecommendCache(request.getUserId());
                } catch (Exception e) {
                    // 缓存清除失败不影响主流程
                }
                
                return R.ok("偏好设置更新成功");
            } else {
                return R.error(400, "偏好设置更新失败，请检查参数");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(500, "系统异常，请稍后重试");
        }
    }
    
    /**
     * 初始化用户偏好设置（向导式）
     * POST /yonghu/preference/init
     * 
     * @param request 初始化请求（包含基础偏好参数）
     * @param httpRequest HTTP请求对象
     * @return 初始化结果
     */
    @RequestMapping("/preference/init")
    public R initPreference(@RequestBody com.dto.PreferenceInitRequest request, HttpServletRequest httpRequest) {
        try {
            // 参数校验
            if (request.getUserId() == null) {
                return R.error(400, "用户ID不能为空");
            }
            
            // 权限校验：仅允许初始化自身偏好
            Long sessionUserId = (Long) httpRequest.getSession().getAttribute("userId");
            if (sessionUserId != null && !sessionUserId.equals(request.getUserId())) {
                return R.error(403, "无权初始化其他用户的偏好设置");
            }
            
            // 执行初始化
            boolean result = yonghuService.initUserPreference(request);
            
            if (result) {
                return R.ok("偏好设置初始化成功");
            } else {
                return R.error(400, "偏好设置初始化失败，请检查参数");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(500, "系统异常，请稍后重试");
        }
    }


}
