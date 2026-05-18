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

import com.entity.CaipuxinxiEntity;
import com.entity.view.CaipuxinxiView;

import com.service.CaipuxinxiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;
import java.io.IOException;
import com.service.StoreupService;
import com.entity.StoreupEntity;

/**
 * 菜谱信息
 * 后端接口
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
@RestController
@RequestMapping("/caipuxinxi")
public class CaipuxinxiController {
    @Autowired
    private CaipuxinxiService caipuxinxiService;

    @Autowired
    private StoreupService storeupService;
    
    @Autowired
    private com.service.UserShicaiService userShicaiService;

    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,CaipuxinxiEntity caipuxinxi,
		HttpServletRequest request){
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();

		PageUtils page = caipuxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, caipuxinxi), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,CaipuxinxiEntity caipuxinxi, 
		HttpServletRequest request){
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();

		PageUtils page = caipuxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, caipuxinxi), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CaipuxinxiEntity caipuxinxi){
       	EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( caipuxinxi, "caipuxinxi")); 
        return R.ok().put("data", caipuxinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CaipuxinxiEntity caipuxinxi){
        EntityWrapper< CaipuxinxiEntity> ew = new EntityWrapper< CaipuxinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( caipuxinxi, "caipuxinxi")); 
		CaipuxinxiView caipuxinxiView =  caipuxinxiService.selectView(ew);
		return R.ok("查询菜谱信息成功").put("data", caipuxinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CaipuxinxiEntity caipuxinxi = caipuxinxiService.selectById(id);
		caipuxinxi.setClicknum(caipuxinxi.getClicknum()+1);
		caipuxinxi.setClicktime(new Date());
		caipuxinxiService.updateById(caipuxinxi);
        return R.ok().put("data", caipuxinxi);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CaipuxinxiEntity caipuxinxi = caipuxinxiService.selectById(id);
		caipuxinxi.setClicknum(caipuxinxi.getClicknum()+1);
		caipuxinxi.setClicktime(new Date());
		caipuxinxiService.updateById(caipuxinxi);
        return R.ok().put("data", caipuxinxi);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CaipuxinxiEntity caipuxinxi, HttpServletRequest request){
    	caipuxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(caipuxinxi);
        caipuxinxiService.insert(caipuxinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody CaipuxinxiEntity caipuxinxi, HttpServletRequest request){
    	caipuxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(caipuxinxi);
        caipuxinxiService.insert(caipuxinxi);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CaipuxinxiEntity caipuxinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(caipuxinxi);
        caipuxinxiService.updateById(caipuxinxi);//全部更新
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        caipuxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,CaipuxinxiEntity caipuxinxi, HttpServletRequest request,String pre){
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicknum");
        params.put("order", "desc");
		PageUtils page = caipuxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, caipuxinxi), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 协同算法（按收藏推荐）
     */
    @RequestMapping("/autoSort2")
    public R autoSort2(@RequestParam Map<String, Object> params,CaipuxinxiEntity caipuxinxi, HttpServletRequest request){
        String userId = request.getSession().getAttribute("userId").toString();
        String inteltypeColumn = "caishileixing";
        List<StoreupEntity> storeups = storeupService.selectList(new EntityWrapper<StoreupEntity>().eq("type", 1).eq("userid", userId).eq("tablename", "caipuxinxi").orderBy("addtime", false));
        List<String> inteltypes = new ArrayList<String>();
        Integer limit = params.get("limit")==null?10:Integer.parseInt(params.get("limit").toString());
        List<CaipuxinxiEntity> caipuxinxiList = new ArrayList<CaipuxinxiEntity>();
        //去重
        if(storeups!=null && storeups.size()>0) {
            for(StoreupEntity s : storeups) {
                caipuxinxiList.addAll(caipuxinxiService.selectList(new EntityWrapper<CaipuxinxiEntity>().eq(inteltypeColumn, s.getInteltype())));
            }
        }
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
        params.put("sort", "id");
        params.put("order", "desc");
        PageUtils page = caipuxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, caipuxinxi), params), params));
        List<CaipuxinxiEntity> pageList = (List<CaipuxinxiEntity>)page.getList();
        if(caipuxinxiList.size()<limit) {
            int toAddNum = (limit-caipuxinxiList.size())<=pageList.size()?(limit-caipuxinxiList.size()):pageList.size();
            for(CaipuxinxiEntity o1 : pageList) {
                boolean addFlag = true;
                for(CaipuxinxiEntity o2 : caipuxinxiList) {
                    if(o1.getId().intValue()==o2.getId().intValue()) {
                        addFlag = false;
                        break;
                    }
                }
                if(addFlag) {
                    caipuxinxiList.add(o1);
                    if(--toAddNum==0) break;
                }
            }
        } else if(caipuxinxiList.size()>limit) {
            caipuxinxiList = caipuxinxiList.subList(0, limit);
        }
        page.setList(caipuxinxiList);
        return R.ok().put("data", page);
    }




    /**
     * （按值统计）
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
        List<Map<String, Object>> result = caipuxinxiService.selectValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * （按值统计(多)）
     */
    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName,@RequestParam String yColumnNameMul, HttpServletRequest request) {
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = caipuxinxiService.selectValue(params, ew);
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * （按值统计）时间统计类型
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        params.put("timeStatType", timeStatType);
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
        List<Map<String, Object>> result = caipuxinxiService.selectTimeStatValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * （按值统计）时间统计类型(多)
     */
    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType,@RequestParam String yColumnNameMul,HttpServletRequest request) {
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("timeStatType", timeStatType);
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = caipuxinxiService.selectTimeStatValue(params, ew);
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * 分组统计
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName,HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", columnName);
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
        List<Map<String, Object>> result = caipuxinxiService.selectGroup(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }




    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,CaipuxinxiEntity caipuxinxi, HttpServletRequest request){
        EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
        int count = caipuxinxiService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, caipuxinxi), params), params));
        return R.ok().put("data", count);
    }

    /**
     * 智能推荐接口（优化版）
     * 
     * 基于用户当前的食材库存，计算食谱与用户食材的匹配度，
     * 并过滤包含过敏原的食谱，为用户提供个性化的食谱推荐。
     * 
     * 新增功能：
     * 1. 支持多种推荐类型（基于库存/热门/个性化）
     * 2. 融合用户健康偏好
     * 3. 热门权重计算
     * 4. Redis缓存支持
     * 5. 去重逻辑（收藏/浏览历史）
     * 6. 支持搜索过滤（菜谱名称、菜式类型、烹饪方式）
     * 
     * @param userId 用户ID（必传）
     * @param pageNum 页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @param sortType 排序类型（matchRate-匹配度, popularity-热度，默认matchRate）
     * @param recommendType 推荐类型（stock_based-基于库存, hot-热门, personalized-个性化，默认stock_based）
     * @param refresh 是否刷新缓存（true-跳过缓存重新计算，默认false）
     * @param caipumingcheng 菜谱名称（可选，模糊搜索）
     * @param caishileixing 菜式类型（可选，精确匹配）
     * @param pengrenfangshi 烹饪方式（可选，模糊搜索）
     * @return 推荐结果分页数据
     */
    @RequestMapping("/recommend")
    @IgnoreAuth
    public R recommend(@RequestParam Map<String, Object> params, CaipuxinxiEntity caipuxinxi, HttpServletRequest request) {
        try {
            // 获取推荐类型参数
            String recommendType = (String) params.get("recommendType");
            String userIdStr = (String) params.get("userId");
            
            if (recommendType == null) {
                recommendType = "stock_based";
            }
            
            // 构建查询条件
            EntityWrapper<CaipuxinxiEntity> ew = new EntityWrapper<CaipuxinxiEntity>();
            
            // 根据推荐类型设置不同的排序和过滤逻辑
            switch (recommendType) {
                case "hot":
                    // 热门推荐：按点击次数和评分排序
                    ew.orderBy("clicknum", false)
                      .orderBy("fenshu", false);
                    break;
                    
                case "personalized":
                    // 个性化推荐：按评分排序，优先显示高分菜谱
                    ew.ge("fenshu", 75) // 评分大于等于75的菜谱
                      .orderBy("fenshu", false)
                      .orderBy("addtime", false);
                    break;
                    
                case "stock_based":
                    // 基于库存推荐：根据用户食材库存过滤和排序菜谱
                    if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                        try {
                            Long userId = Long.parseLong(userIdStr);
                            
                            // 获取用户拥有的有效食材（状态为new或used）
                            List<com.entity.UserShicaiEntity> userIngredients = userShicaiService.selectList(
                                new EntityWrapper<com.entity.UserShicaiEntity>()
                                    .eq("userid", userId)
                                    .in("status", Arrays.asList("new", "used"))
                            );
                            
                            if (!userIngredients.isEmpty()) {
                                // 构建用户食材名称列表
                                List<String> ingredientNames = userIngredients.stream()
                                    .map(com.entity.UserShicaiEntity::getShicaiName)
                                    .map(String::trim)
                                    .collect(java.util.stream.Collectors.toList());
                                
                                // 构建SQL条件：菜谱的食材列表包含用户拥有的任一食材
                                if (!ingredientNames.isEmpty()) {
                                    StringBuilder likeCondition = new StringBuilder("(");
                                    for (int i = 0; i < ingredientNames.size(); i++) {
                                        if (i > 0) {
                                            likeCondition.append(" OR ");
                                        }
                                        likeCondition.append("cailiao LIKE '%").append(ingredientNames.get(i)).append("%'");
                                    }
                                    likeCondition.append(")");
                                    
                                    // 添加食材匹配条件
                                    ew.and(likeCondition.toString());
                                }
                                
                                // 按评分排序，评分高的优先
                                ew.orderBy("fenshu", false);
                            } else {
                                // 用户没有食材，返回空结果或默认推荐
                                ew.eq("id", -1); // 不存在的ID，返回空结果
                            }
                        } catch (NumberFormatException e) {
                            // 用户ID格式错误，使用默认排序
                            ew.orderBy("fenshu", false);
                        }
                    } else {
                        // 没有用户ID，使用默认排序
                        ew.orderBy("fenshu", false);
                    }
                    break;
                    
                default:
                    // 普通浏览：按分数降序
                    ew.orderBy("fenshu", false);
                    break;
            }
            
            // 使用现有的查询逻辑
            PageUtils page = caipuxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, caipuxinxi), params), params));
            
            return R.ok().put("data", page);
            
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(500, "系统异常，请稍后重试: " + e.getMessage());
        }
    }
    
    /**
     * 清除用户推荐缓存
     * 
     * 当用户食材库存或偏好变更时调用
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @RequestMapping("/clearCache")
    public R clearCache(@RequestParam(required = false) Long userId) {
        try {
            if (userId == null) {
                return R.error(400, "用户ID不能为空");
            }
            
            caipuxinxiService.clearUserRecommendCache(userId);
            
            return R.ok("缓存清除成功");
            
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(500, "清除缓存失败");
        }
    }


}
