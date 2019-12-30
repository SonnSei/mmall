package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @Classname ICategoryService
 * @Description TODO
 * @Date 2019/12/28 15:18
 * @Author SonnSei
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);

    ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId);
}
