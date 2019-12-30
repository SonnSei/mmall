package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @Classname ICartService
 * @Description TODO
 * @Date 2019/12/30 21:08
 * @Author SonnSei
 */
public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
}
