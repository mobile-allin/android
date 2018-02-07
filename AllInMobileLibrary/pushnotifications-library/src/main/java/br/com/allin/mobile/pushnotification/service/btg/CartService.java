package br.com.allin.mobile.pushnotification.service.btg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.CartEntity;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class CartService extends TrackingBaseService<CartEntity> {
    public CartService(String account, List<CartEntity> carts) {
        super(account, "cart", carts);
    }

    @Override
    JSONArray transform(List<CartEntity> list) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (CartEntity cart : list) {
                JSONObject productJSONObject = new JSONObject();
                productJSONObject.put("productId", cart.getProductId());

                jsonArray.put(productJSONObject);
            }

            return jsonArray;
        } catch (Exception e) {
            return null;
        }
    }
}
