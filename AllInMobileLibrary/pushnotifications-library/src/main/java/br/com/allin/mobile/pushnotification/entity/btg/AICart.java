package br.com.allin.mobile.pushnotification.entity.btg;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class AICart {
    private String productId;

    public AICart(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
