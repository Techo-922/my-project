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
 * 用户食材库
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
@TableName("user_shicai")
public class UserShicaiEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public UserShicaiEntity() {
		
	}
	
	public UserShicaiEntity(T t) {
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
	 * 食材名称
	 */
	private String shicaiName;
	
	/**
	 * 数量
	 */
	private Integer quantity;
	
	/**
	 * 单位
	 */
	private String unit;
	
	/**
	 * 购买日期
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date purchaseDate;
	
	/**
	 * 过期日期
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date expiryDate;
	
	/**
	 * 状态：new-新鲜, used-已使用, expired-已过期, discarded-已丢弃
	 */
	private String status;
	
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

	public String getShicaiName() {
		return shicaiName;
	}

	public void setShicaiName(String shicaiName) {
		this.shicaiName = shicaiName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
}
