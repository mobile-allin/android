package br.com.allin.mobile.pushnotification;

import android.content.Context;

import java.util.Collections;
import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.AICart;
import br.com.allin.mobile.pushnotification.entity.btg.AIClient;
import br.com.allin.mobile.pushnotification.entity.btg.AIProduct;
import br.com.allin.mobile.pushnotification.entity.btg.AISearch;
import br.com.allin.mobile.pushnotification.entity.btg.AITransaction;
import br.com.allin.mobile.pushnotification.entity.btg.AIWarn;
import br.com.allin.mobile.pushnotification.entity.btg.AIWish;
import br.com.allin.mobile.pushnotification.service.btg.CartService;
import br.com.allin.mobile.pushnotification.service.btg.ClientService;
import br.com.allin.mobile.pushnotification.service.btg.ProductService;
import br.com.allin.mobile.pushnotification.service.btg.SearchService;
import br.com.allin.mobile.pushnotification.service.btg.TransactionService;
import br.com.allin.mobile.pushnotification.service.btg.WarnMeService;
import br.com.allin.mobile.pushnotification.service.btg.WishListService;

/**
 * Created by lucasrodrigues on 06/02/18.
 */
public class BTG360 {
    public static void initialize(Context context) {
        AlliNPush.getInstance().setContext(context);
    }

    public static void setDeviceToken(String deviceToken) {
        AlliNPush.getInstance().setDeviceToken(deviceToken);
    }

    public static void addProduct(String account, AIProduct product) {
        BTG360.addProducts(account, Collections.singletonList(product));
    }

    public static void addProducts(String account, List<AIProduct> products) {
        new ProductService(account, products).send();
    }

    public static void addCart(String account, AICart cart) {
        BTG360.addCarts(account, Collections.singletonList(cart));
    }

    public static void addCarts(String account, List<AICart> carts) {
        new CartService(account, carts).send();
    }

    public static void addClient(String account, AIClient client) {
        BTG360.addClients(account, Collections.singletonList(client));
    }

    public static void addClients(String account, List<AIClient> clients) {
        new ClientService(account, clients).send();
    }

    public static void addTransaction(String account, AITransaction transaction) {
        BTG360.addTransactions(account, Collections.singletonList(transaction));
    }

    public static void addTransactions(String account, List<AITransaction> transactions) {
        new TransactionService(account, transactions).send();
    }

    public static void addSearch(String account, AISearch search) {
        BTG360.addSearchs(account, Collections.singletonList(search));
    }

    public static void addSearchs(String account, List<AISearch> searchs) {
        new SearchService(account, searchs).send();
    }

    public static void addWishList(String account, AIWish wish) {
        BTG360.addWishList(account, Collections.singletonList(wish));
    }

    public static void addWishList(String account, List<AIWish> wishs) {
        new WishListService(account, wishs).send();
    }

    public static void addWarnMe(String account, AIWarn warn) {
        BTG360.addWarnMe(account, Collections.singletonList(warn));
    }

    public static void addWarnMe(String account, List<AIWarn> warns) {
        new WarnMeService(account, warns).send();
    }
}
