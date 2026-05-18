package com.service;

import com.dto.ShoppingListRequest;
import com.dto.ShoppingListResponse;

/**
 * 智能采购清单服务
 * 
 * @author 
 * @date 2023-04-25
 */
public interface ShoppingService {
    
    /**
     * 生成采购建议清单
     * 
     * @param request 采购清单请求参数
     * @return 采购清单响应
     */
    ShoppingListResponse generateShoppingList(ShoppingListRequest request);
    
    /**
     * 导出采购清单为JSON格式
     * 
     * @param userId 用户ID
     * @param listId 清单ID（可选）
     * @return JSON字符串
     */
    String exportToJson(Long userId, Long listId);
}
