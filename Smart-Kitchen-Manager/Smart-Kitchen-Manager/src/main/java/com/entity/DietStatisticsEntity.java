package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * 饮食统计
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
@TableName("diet_statistics")
public class DietStatisticsEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public DietStatisticsEntity() {
		
	}
	
	public DietStatisticsEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	
	/**
	 * 用户id
	 */
	private Long userid;
	
	/**
	 * 记录日期
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date recordDate;
	
	/**
	 * 餐次类型
	 */
	private String mealType;
	
	/**
	 * 食物名称
	 */
	private String foodName;
	
	/**
	 * 卡路里
	 */
	private Integer calories;
	
	/**
	 * 蛋白质(g)
	 */
	private Double protein;
	
	/**
	 * 碳水化合物(g)
	 */
	private Double carbs;
	
	/**
	 * 脂肪(g)
	 */
	private Double fat;
	
	/**
	 * 备注
	 */
	private String notes;
	
	/**
	 * 添加时间
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public Double getProtein() {
		return protein;
	}

	public void setProtein(Double protein) {
		this.protein = protein;
	}

	public Double getCarbs() {
		return carbs;
	}

	public void setCarbs(Double carbs) {
		this.carbs = carbs;
	}

	public Double getFat() {
		return fat;
	}

	public void setFat(Double fat) {
		this.fat = fat;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
}
