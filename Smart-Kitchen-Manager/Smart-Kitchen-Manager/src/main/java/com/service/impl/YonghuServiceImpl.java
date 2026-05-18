package com.service.impl;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import com.dao.YonghuDao;
import com.entity.YonghuEntity;
import com.service.YonghuService;
import com.entity.vo.YonghuVO;
import com.entity.view.YonghuView;
import com.dto.UserPreferenceDTO;
import com.dto.PreferenceInitRequest;
import com.dto.PreferenceUpdateRequest;

@Service("yonghuService")
public class YonghuServiceImpl extends ServiceImpl<YonghuDao, YonghuEntity> implements YonghuService {
	
	private static final Logger log = LoggerFactory.getLogger(YonghuServiceImpl.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<YonghuEntity> page = this.selectPage(
                new Query<YonghuEntity>(params).getPage(),
                new EntityWrapper<YonghuEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<YonghuEntity> wrapper) {
		  Page<YonghuView> page =new Query<YonghuView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<YonghuVO> selectListVO(Wrapper<YonghuEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public YonghuVO selectVO(Wrapper<YonghuEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<YonghuView> selectListView(Wrapper<YonghuEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public YonghuView selectView(Wrapper<YonghuEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}

	/**
	 * 获取用户偏好设置
	 * 
	 * @param userId 用户ID
	 * @return 用户偏好DTO
	 */
	@Override
	public UserPreferenceDTO getUserPreference(Long userId) {
		log.info("获取用户偏好设置 - 用户ID: {}", userId);
		
		YonghuEntity user = this.selectById(userId);
		if (user == null) {
			log.warn("用户不存在 - 用户ID: {}", userId);
			return null;
		}
		
		UserPreferenceDTO dto = new UserPreferenceDTO();
		dto.setUserId(userId);
		dto.setHealthPreference(user.getHealthPreference());
		dto.setAllergyInfo(user.getAllergyInfo());
		
		log.debug("用户偏好设置获取成功 - 用户ID: {}", userId);
		return dto;
	}

	/**
	 * 更新用户偏好设置
	 * 
	 * @param request 更新请求
	 * @return 是否成功
	 */
	@Override
	public boolean updateUserPreference(PreferenceUpdateRequest request) {
		log.info("更新用户偏好设置 - 用户ID: {}", request.getUserId());
		
		try {
			// 验证用户是否存在
			YonghuEntity user = this.selectById(request.getUserId());
			if (user == null) {
				log.warn("用户不存在 - 用户ID: {}", request.getUserId());
				return false;
			}
			
			// 验证JSON格式（如果提供了healthPreference）
			if (request.getHealthPreference() != null && !request.getHealthPreference().trim().isEmpty()) {
				if (!isValidJson(request.getHealthPreference())) {
					log.error("健康偏好JSON格式非法 - 用户ID: {}", request.getUserId());
					return false;
				}
				user.setHealthPreference(request.getHealthPreference());
			}
			
			// 更新过敏信息
			if (request.getAllergyInfo() != null) {
				user.setAllergyInfo(request.getAllergyInfo().trim());
			}
			
			// 执行更新
			boolean result = this.updateById(user);
			
			if (result) {
				log.info("用户偏好设置更新成功 - 用户ID: {}", request.getUserId());
			} else {
				log.error("用户偏好设置更新失败 - 用户ID: {}", request.getUserId());
			}
			
			return result;
			
		} catch (Exception e) {
			log.error("更新用户偏好设置时发生异常 - 用户ID: {}", request.getUserId(), e);
			return false;
		}
	}

	/**
	 * 初始化用户偏好设置（向导式）
	 * 
	 * @param request 初始化请求
	 * @return 是否成功
	 */
	@Override
	public boolean initUserPreference(PreferenceInitRequest request) {
		log.info("初始化用户偏好设置 - 用户ID: {}", request.getUserId());
		
		try {
			// 验证用户是否存在
			YonghuEntity user = this.selectById(request.getUserId());
			if (user == null) {
				log.warn("用户不存在 - 用户ID: {}", request.getUserId());
				return false;
			}
			
			// 构建健康偏好JSON
			Map<String, Object> healthPreferenceMap = new HashMap<>();
			
			// 饮食类型
			if (request.getDietType() != null && !request.getDietType().trim().isEmpty()) {
				healthPreferenceMap.put("dietType", request.getDietType());
			}
			
			// 营养目标
			Map<String, Integer> nutrientGoal = new HashMap<>();
			if (request.getProteinGoal() != null && request.getProteinGoal() > 0) {
				nutrientGoal.put("protein", request.getProteinGoal());
			}
			if (request.getCarbGoal() != null && request.getCarbGoal() > 0) {
				nutrientGoal.put("carb", request.getCarbGoal());
			}
			if (request.getFatGoal() != null && request.getFatGoal() > 0) {
				nutrientGoal.put("fat", request.getFatGoal());
			}
			if (!nutrientGoal.isEmpty()) {
				healthPreferenceMap.put("nutrientGoal", nutrientGoal);
			}
			
			// 口味偏好
			if (request.getFlavor() != null && !request.getFlavor().trim().isEmpty()) {
				healthPreferenceMap.put("flavor", request.getFlavor());
			}
			
			// 转换为JSON字符串
			String healthPreferenceJson = objectMapper.writeValueAsString(healthPreferenceMap);
			user.setHealthPreference(healthPreferenceJson);
			
			// 设置过敏信息
			if (request.getAllergyInfo() != null) {
				user.setAllergyInfo(request.getAllergyInfo().trim());
			}
			
			// 执行更新
			boolean result = this.updateById(user);
			
			if (result) {
				log.info("用户偏好设置初始化成功 - 用户ID: {}, JSON: {}", 
					request.getUserId(), healthPreferenceJson);
			} else {
				log.error("用户偏好设置初始化失败 - 用户ID: {}", request.getUserId());
			}
			
			return result;
			
		} catch (JsonProcessingException e) {
			log.error("生成健康偏好JSON时发生异常 - 用户ID: {}", request.getUserId(), e);
			return false;
		} catch (Exception e) {
			log.error("初始化用户偏好设置时发生异常 - 用户ID: {}", request.getUserId(), e);
			return false;
		}
	}

	/**
	 * 验证JSON格式是否合法
	 * 
	 * @param jsonStr JSON字符串
	 * @return 是否合法
	 */
	private boolean isValidJson(String jsonStr) {
		try {
			objectMapper.readTree(jsonStr);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


}
