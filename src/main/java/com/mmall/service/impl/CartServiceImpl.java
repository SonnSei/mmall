package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname CartServiceImpl
 * @Description TODO
 * @Date 2019/12/30 21:08
 * @Author SonnSei
 */
@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;



    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart == null) {
            // 这个产品没有记录，需要新增一个产品
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        } else {
            // 这个产品已经存在，数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return list(userId);

    }

    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        // 如何避免丢失精度, BigDecimal的String构造器
        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProcductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    // 判断库存
                    int buLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        buLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuality(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buLimitCount = product.getStock();
                        cartProductVo.setProcductName(Const.Cart.LIMIT_NUM_FAIL);
                        // 购物车中更新有效库存
                        Cart cartForQuality = new Cart();
                        cartForQuality.setId(cartItem.getId());
                        cartForQuality.setQuantity(buLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuality);
                    }
                    cartProductVo.setQuality(buLimitCount);
                    // 计算总价
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.multiply(product.getPrice().doubleValue(), cartProductVo.getQuality()));

                    cartProductVo.setProductChecked(cartItem.getChecked());

                }
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    // 如果勾选，增加到整个购物车的总价
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(getAllCheckedStatus(userId));
        cartVo.setIamgeHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckStatusByUserId(userId) == 0;
    }

    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return list(userId);
    }

    public ServerResponse<CartVo> delete(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (productIdList.isEmpty()) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId, productIdList);
        return list(userId);
    }


    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo = getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    public ServerResponse<CartVo> selectOrUnselectAll(Integer userId,Integer checked) {
        cartMapper.checkOrUncheckedAllProduct(userId, checked);
        return list(userId);
    }

    public ServerResponse<CartVo> selectOrUnselect(Integer userId,Integer productId,Integer checked) {
        cartMapper.checkOrUncheckedProduct(userId,productId, checked);
        return list(userId);
    }

    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null ) {
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.getCartProductCount(userId));
    }






}
