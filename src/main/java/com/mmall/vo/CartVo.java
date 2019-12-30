package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname CartVo
 * @Description TODO
 * @Date 2019/12/30 21:32
 * @Author SonnSei
 */
public class CartVo {
    List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    private String iamgeHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getIamgeHost() {
        return iamgeHost;
    }

    public void setIamgeHost(String iamgeHost) {
        this.iamgeHost = iamgeHost;
    }
}
