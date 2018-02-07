package br.com.allin.mobile.pushnotification.service.btg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.ProductEntity;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class ProductService extends TrackingBaseService<ProductEntity> {
    public ProductService(String account, List<ProductEntity> products) {
        super(account, "product", products);
    }

    @Override
    JSONArray transform(List<ProductEntity> list) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (ProductEntity product : list) {
                JSONObject productJSONObject = new JSONObject();
                productJSONObject.put("productId", product.getProductId());

                jsonArray.put(productJSONObject);
            }

            return jsonArray;
        } catch (Exception e) {
            return null;
        }
    }
}
