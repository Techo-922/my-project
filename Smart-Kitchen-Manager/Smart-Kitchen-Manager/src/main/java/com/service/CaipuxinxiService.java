package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.CaipuxinxiEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.CaipuxinxiVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.CaipuxinxiView;


/**
 * 菜谱信息
 *
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
public interface CaipuxinxiService extends IService<CaipuxinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<CaipuxinxiVO> selectListVO(Wrapper<CaipuxinxiEntity> wrapper);
   	
   	CaipuxinxiVO selectVO(@Param("ew") Wrapper<CaipuxinxiEntity> wrapper);
   	
   	List<CaipuxinxiView> selectListView(Wrapper<CaipuxinxiEntity> wrapper);
   	
   	CaipuxinxiView selectView(@Param("ew") Wrapper<CaipuxinxiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<CaipuxinxiEntity> wrapper);
   	

    List<Map<String, Object>> selectValue(Map<String, Object> params,Wrapper<CaipuxinxiEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,Wrapper<CaipuxinxiEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,Wrapper<CaipuxinxiEntity> wrapper);

    /**
     * 获取智能推荐列表（基础版）
     * 
     * @param userId 用户ID
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @param sortType 排序类型（matchRate-匹配度, popularity-热度）
     * @return 推荐结果分页数据
     */
    PageUtils getRecommendations(Long userId, Integer pageNum, Integer pageSize, String sortType);

    /**
     * 获取智能推荐列表（优化版，支持缓存）
     * 
     * 基于用户当前的食材库存，计算食谱与用户食材的匹配度，
     * 并过滤包含过敏原的食谱，为用户提供个性化的食谱推荐。
     * 
     * @param userId 用户ID
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @param sortType 排序类型（matchRate-匹配度, popularity-热度）
     * @param recommendType 推荐类型（stock_based-基于库存, hot-热门, personalized-个性化）
     * @param refresh 是否刷新缓存（true-跳过缓存重新计算）
     * @return 推荐结果分页数据
     */
    PageUtils getRecommendations(Long userId, Integer pageNum, Integer pageSize, String sortType, String recommendType, Boolean refresh);

    /**
     * 获取智能推荐列表（优化版，支持缓存和搜索过滤）
     * 
     * 基于用户当前的食材库存，计算食谱与用户食材的匹配度，
     * 并过滤包含过敏原的食谱，为用户提供个性化的食谱推荐。
     * 
     * @param userId 用户ID
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @param sortType 排序类型（matchRate-匹配度, popularity-热度）
     * @param recommendType 推荐类型（stock_based-基于库存, hot-热门, personalized-个性化）
     * @param refresh 是否刷新缓存（true-跳过缓存重新计算）
     * @param searchParams 搜索参数（caipumingcheng、caishileixing、pengrenfangshi）
     * @return 推荐结果分页数据
     */
    PageUtils getRecommendations(Long userId, Integer pageNum, Integer pageSize, String sortType, String recommendType, Boolean refresh, Map<String, Object> searchParams);

    /**
     * 清除用户推荐缓存
     * 
     * 当用户食材库存或偏好变更时调用，清除相关缓存
     * 
     * @param userId 用户ID
     */
    void clearUserRecommendCache(Long userId);

    /**
     * 清除热门食谱缓存
     * 
     * 当食谱数据变更时调用
     */
    void clearHotRecipesCache();


}

