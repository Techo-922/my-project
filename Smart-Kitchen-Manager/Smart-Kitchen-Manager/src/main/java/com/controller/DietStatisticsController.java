package com.controller;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
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
import com.entity.DietStatisticsEntity;
import com.service.DietStatisticsService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 饮食统计
 * 后端接口
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
@RestController
@RequestMapping("/dietstatistics")
public class DietStatisticsController {
    @Autowired
    private DietStatisticsService dietStatisticsService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, DietStatisticsEntity dietStatistics,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			dietStatistics.setUserid((Long)request.getSession().getAttribute("userId"));
		}
        EntityWrapper<DietStatisticsEntity> ew = new EntityWrapper<DietStatisticsEntity>();

		PageUtils page = dietStatisticsService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dietStatistics), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, DietStatisticsEntity dietStatistics, 
		HttpServletRequest request){
        EntityWrapper<DietStatisticsEntity> ew = new EntityWrapper<DietStatisticsEntity>();

		PageUtils page = dietStatisticsService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dietStatistics), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(DietStatisticsEntity dietStatistics){
       	EntityWrapper<DietStatisticsEntity> ew = new EntityWrapper<DietStatisticsEntity>();
      	ew.allEq(MPUtil.allEQMapPre(dietStatistics, "dietstatistics")); 
        return R.ok().put("data", dietStatisticsService.selectList(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DietStatisticsEntity dietStatistics){
        EntityWrapper<DietStatisticsEntity> ew = new EntityWrapper<DietStatisticsEntity>();
 		ew.allEq(MPUtil.allEQMapPre(dietStatistics, "dietstatistics")); 
		DietStatisticsEntity dietStatisticsEntity = dietStatisticsService.selectOne(ew);
		return R.ok("查询饮食统计成功").put("data", dietStatisticsEntity);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DietStatisticsEntity dietStatistics = dietStatisticsService.selectById(id);
        return R.ok().put("data", dietStatistics);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DietStatisticsEntity dietStatistics = dietStatisticsService.selectById(id);
        return R.ok().put("data", dietStatistics);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DietStatisticsEntity dietStatistics, HttpServletRequest request){
    	dietStatistics.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			dietStatistics.setUserid((Long)request.getSession().getAttribute("userId"));
		}
		
		dietStatistics.setAddtime(new Date());
        dietStatisticsService.insert(dietStatistics);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DietStatisticsEntity dietStatistics, HttpServletRequest request){
    	dietStatistics.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	
		dietStatistics.setAddtime(new Date());
        dietStatisticsService.insert(dietStatistics);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DietStatisticsEntity dietStatistics, HttpServletRequest request){
        dietStatisticsService.updateById(dietStatistics);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        dietStatisticsService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 根据用户ID查询所有记录
     */
    @RequestMapping("/listByUser")
    public R listByUser(@RequestParam Long userid) {
        EntityWrapper<DietStatisticsEntity> ew = new EntityWrapper<DietStatisticsEntity>();
        ew.eq("userid", userid);
        ew.orderBy("record_date", false);
        return R.ok().put("data", dietStatisticsService.selectList(ew));
    }
    
    /**
     * 根据用户ID和日期范围查询统计
     */
    @RequestMapping("/listByDateRange")
    public R listByDateRange(@RequestParam Long userid, 
                             @RequestParam String startDate, 
                             @RequestParam String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<DietStatisticsEntity> ew = new EntityWrapper<DietStatisticsEntity>();
        ew.eq("userid", userid);
        ew.between("record_date", sdf.parse(startDate), sdf.parse(endDate));
        ew.orderBy("record_date", false);
        return R.ok().put("data", dietStatisticsService.selectList(ew));
    }
    
    /**
     * 获取用户最近7天的统计
     */
    @RequestMapping("/listRecent7Days")
    public R listRecent7Days(@RequestParam Long userid){
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();
        
        EntityWrapper<DietStatisticsEntity> ew = new EntityWrapper<DietStatisticsEntity>();
        ew.eq("userid", userid);
        ew.between("record_date", startDate, endDate);
        ew.orderBy("record_date", false);
        return R.ok().put("data", dietStatisticsService.selectList(ew));
    }
    
    /**
     * 获取用户最近30天的统计
     */
    @RequestMapping("/listRecent30Days")
    public R listRecent30Days(@RequestParam Long userid){
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date startDate = calendar.getTime();
        
        EntityWrapper<DietStatisticsEntity> ew = new EntityWrapper<DietStatisticsEntity>();
        ew.eq("userid", userid);
        ew.between("record_date", startDate, endDate);
        ew.orderBy("record_date", false);
        return R.ok().put("data", dietStatisticsService.selectList(ew));
    }
    
    /**
     * 按食材统计消耗量
     */
    @RequestMapping("/groupByShicai")
    public R groupByShicai(@RequestParam Long userid, 
                           @RequestParam(required = false) String startDate,
                           @RequestParam(required = false) String endDate) throws ParseException {
        EntityWrapper<DietStatisticsEntity> ew = new EntityWrapper<DietStatisticsEntity>();
        ew.eq("userid", userid);
        
        if(startDate != null && endDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ew.between("record_date", sdf.parse(startDate), sdf.parse(endDate));
        }
        
        ew.groupBy("food_name");
        ew.setSqlSelect("food_name, SUM(calories) as calories, SUM(protein) as protein, SUM(carbs) as carbs, SUM(fat) as fat");
        
        List<DietStatisticsEntity> list = dietStatisticsService.selectList(ew);
        return R.ok().put("data", list);
    }

}
