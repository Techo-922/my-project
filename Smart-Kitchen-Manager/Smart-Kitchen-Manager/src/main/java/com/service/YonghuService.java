package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.YonghuEntity;
import com.dto.UserPreferenceDTO;
import com.dto.PreferenceInitRequest;
import com.dto.PreferenceUpdateRequest;
import java.util.List;
import java.util.Map;
import com.entity.vo.YonghuVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.YonghuView;


/**
 * 用户
 *
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
public interface YonghuService extends IService<YonghuEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<YonghuVO> selectListVO(Wrapper<YonghuEntity> wrapper);
   	
   	YonghuVO selectVO(@Param("ew") Wrapper<YonghuEntity> wrapper);
   	
   	List<YonghuView> selectListView(Wrapper<YonghuEntity> wrapper);
   	
   	YonghuView selectView(@Param("ew") Wrapper<YonghuEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<YonghuEntity> wrapper);
   	
   	/**
   	 * 获取用户偏好设置
   	 * 
   	 * @param userId 用户ID
   	 * @return 用户偏好DTO
   	 */
   	UserPreferenceDTO getUserPreference(Long userId);
   	
   	/**
   	 * 更新用户偏好设置
   	 * 
   	 * @param request 更新请求
   	 * @return 是否成功
   	 */
   	boolean updateUserPreference(PreferenceUpdateRequest request);
   	
   	/**
   	 * 初始化用户偏好设置（向导式）
   	 * 
   	 * @param request 初始化请求
   	 * @return 是否成功
   	 */
   	boolean initUserPreference(PreferenceInitRequest request);

}

