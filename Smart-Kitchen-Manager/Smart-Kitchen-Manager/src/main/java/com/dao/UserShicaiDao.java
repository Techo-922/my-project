package com.dao;

import com.entity.UserShicaiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;

/**
 * 用户食材库
 * 
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
public interface UserShicaiDao extends BaseMapper<UserShicaiEntity> {
	
	/**
	 * 查询用户有效食材（状态为'new'或'used'）
	 * 
	 * @param userid 用户ID
	 * @return 用户有效食材列表
	 */
	List<UserShicaiEntity> selectValidIngredientsByUserId(@Param("userid") Long userid);
	
}
