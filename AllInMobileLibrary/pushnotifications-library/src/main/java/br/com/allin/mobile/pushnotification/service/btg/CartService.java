package br.com.allin.mobile.pushnotification.service.btg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.AICart;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class CartService extends TrackingBaseService<AICart> {
    public CartService(String account, List<AICart> carts) {
        super(account, "cart", carts);
    }

    @Override
    JSONArray transform(List<AICart> list) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (AICart cart : list) {
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
