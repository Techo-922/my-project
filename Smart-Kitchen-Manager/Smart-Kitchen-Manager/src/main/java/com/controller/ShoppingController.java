package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.ShoppingService;
import com.dto.ShoppingListRequest;
import com.dto.ShoppingListResponse;
import com.utils.R;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 智能采购清单控制器
 * 
 * @author 
 * @date 2023-04-25
 */
@RestController
@RequestMapping("/shopping")
public class ShoppingController {
    
    @Autowired
    private ShoppingService shoppingService;
    
    /**
     * 生成采购建议清单
     * GET /shopping/suggest
     * 
     * @param userId 用户ID（必填）
     * @param recipeIds 目标食谱ID列表（可选，逗号分隔）
     * @param threshold 补充库存阈值（可选，默认0.3）
     * @param includeSafetyStock 是否包含安全库存（可选，默认true）
     * @param safetyStockFactor 安全库存系数（可选，默认1.2）
     * @return 采购清单
     */
    @RequestMapping("/suggest")
    public R suggest(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String recipeIds,
        @RequestParam(required = false, defaultValue = "0.3") Double threshold,
        @RequestParam(required = false, defaultValue = "true") Boolean includeSafetyStock,
        @RequestParam(required = false, defaultValue = "1.2") Double safetyStockFactor,
        HttpServletRequest request
    ) {
        try {
            // 参数验证
            if (userId == null) {
                return R.error(400, "用户ID不能为空");
            }
            
            // 构建请求对象
            ShoppingListRequest shoppingRequest = new ShoppingListRequest();
            shoppingRequest.setUserId(userId);
            shoppingRequest.setThreshold(threshold);
            shoppingRequest.setIncludeSafetyStock(includeSafetyStock);
            shoppingRequest.setSafetyStockFactor(safetyStockFactor);
            
            // 解析食谱ID列表
            if (recipeIds != null && !recipeIds.trim().isEmpty()) {
                String[] idArray = recipeIds.split(",");
                java.util.List<Long> recipeIdList = new java.util.ArrayList<>();
                for (String idStr : idArray) {
                    try {
                        recipeIdList.add(Long.parseLong(idStr.trim()));
                    } catch (NumberFormatException e) {
                        return R.error(400, "食谱ID格式错误: " + idStr);
                    }
                }
                shoppingRequest.setRecipeIds(recipeIdList);
            }
            
            // 生成采购清单
            ShoppingListResponse response = shoppingService.generateShoppingList(shoppingRequest);
            
            return R.ok().put("data", response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(500, "生成采购清单失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成采购建议清单（POST方式）
     * POST /shopping/suggest
     * 
     * @param shoppingRequest 采购清单请求对象
     * @return 采购清单
     */
    @RequestMapping(value = "/suggest", method = RequestMethod.POST)
    public R suggestPost(@RequestBody ShoppingListRequest shoppingRequest) {
        try {
            // 参数验证
            if (shoppingRequest.getUserId() == null) {
                return R.error(400, "用户ID不能为空");
            }
            
            // 生成采购清单
            ShoppingListResponse response = shoppingService.generateShoppingList(shoppingRequest);
            
            return R.ok().put("data", response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(500, "生成采购清单失败: " + e.getMessage());
        }
    }
    
    /**
     * 导出采购清单
     * POST /shopping/export
     * 
     * @param userId 用户ID（必填）
     * @param listId 清单ID（可选）
     * @param format 导出格式（json/excel，默认json）
     * @param response HTTP响应对象
     */
    @RequestMapping("/export")
    public void export(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long listId,
        @RequestParam(required = false, defaultValue = "json") String format,
        HttpServletResponse response
    ) {
        try {
            // 参数验证
            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write("{\"code\":400,\"msg\":\"用户ID不能为空\"}");
                writer.flush();
                return;
            }
            
            // 导出为JSON格式
            if ("json".equalsIgnoreCase(format)) {
                String json = shoppingService.exportToJson(userId, listId);
                
                // 设置响应头
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Content-Disposition", 
                    "attachment; filename=shopping_list_" + userId + ".json");
                
                // 写入响应
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
                
            } else if ("excel".equalsIgnoreCase(format)) {
                // Excel导出功能（暂未实现，返回提示）
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write("{\"code\":501,\"msg\":\"Excel导出功能暂未实现\"}");
                writer.flush();
                
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write("{\"code\":400,\"msg\":\"不支持的导出格式: " + format + "\"}");
                writer.flush();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write("{\"code\":500,\"msg\":\"导出采购清单失败: " + e.getMessage() + "\"}");
                writer.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
    /**
     * 获取采购清单统计信息
     * GET /shopping/stats
     * 
     * @param userId 用户ID（必填）
     * @return 统计信息
     */
    @RequestMapping("/stats")
    public R stats(@RequestParam(required = false) Long userId) {
        try {
            // 参数验证
            if (userId == null) {
                return R.error(400, "用户ID不能为空");
            }
            
            // 生成默认采购清单
            ShoppingListRequest request = new ShoppingListRequest();
            request.setUserId(userId);
            
            ShoppingListResponse response = shoppingService.generateShoppingList(request);
            
            // 构建统计信息
            java.util.Map<String, Object> stats = new java.util.HashMap<>();
            stats.put("userId", response.getUserId());
            stats.put("totalItems", response.getTotalItems());
            stats.put("mustBuyItems", response.getMustBuyItems());
            stats.put("optionalItems", response.getOptionalItems());
            stats.put("categoryCount", response.getItemsByCategory().size());
            stats.put("categories", response.getItemsByCategory().keySet());
            stats.put("generateTime", response.getGenerateTime());
            
            return R.ok().put("data", stats);
            
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(500, "获取统计信息失败: " + e.getMessage());
        }
    }
}
