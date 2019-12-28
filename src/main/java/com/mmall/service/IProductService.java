package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * @Classname IProductService
 * @Description TODO
 * @Date 2019/12/28 18:42
 * @Author SonnSei
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);
    ServerResponse getDetail(Integer productId);
}
