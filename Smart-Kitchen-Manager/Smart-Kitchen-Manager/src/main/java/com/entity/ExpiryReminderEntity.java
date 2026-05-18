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
 * 过期提醒
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2023-04-25 08:11:08
 */
@TableName("expiry_reminder")
public class ExpiryReminderEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public ExpiryReminderEntity() {
		
	}
	
	public ExpiryReminderEntity(T t) {
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
	 * 用户食材id
	 */
	private Long userShicaiId;
	
	/**
	 * 提醒日期
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date remindDate;
	
	/**
	 * 状态：pending-待发送, sent-已发送, read-已读
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

	public Long getUserShicaiId() {
		return userShicaiId;
	}

	public void setUserShicaiId(Long userShicaiId) {
		this.userShicaiId = userShicaiId;
	}

	public Date getRemindDate() {
		return remindDate;
	}

	public void setRemindDate(Date remindDate) {
		this.remindDate = remindDate;
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
