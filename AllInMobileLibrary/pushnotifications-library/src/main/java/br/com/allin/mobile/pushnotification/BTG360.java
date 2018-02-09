package br.com.allin.mobile.pushnotification;

import java.util.Collections;
import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.CartEntity;
import br.com.allin.mobile.pushnotification.entity.btg.ClientEntity;
import br.com.allin.mobile.pushnotification.entity.btg.ProductEntity;
import br.com.allin.mobile.pushnotification.entity.btg.SearchEntity;
import br.com.allin.mobile.pushnotification.entity.btg.TransactionEntity;
import br.com.allin.mobile.pushnotification.entity.btg.WarnMeEntity;
import br.com.allin.mobile.pushnotification.entity.btg.WishListEntity;
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
    public static void addProduct(String account, ProductEntity product) {
        BTG360.addProducts(account, Collections.singletonList(product));
    }

    public static void addProducts(String account, List<ProductEntity> products) {
        new ProductService(account, products).send();
    }

    public static void addCart(String account, CartEntity cart) {
        BTG360.addCarts(account, Collections.singletonList(cart));
    }

    public static void addCarts(String account, List<CartEntity> carts) {
        new CartService(account, carts).send();
    }

    public static void addClient(String account, ClientEntity client) {
        BTG360.addClients(account, Collections.singletonList(client));
    }

    public static void addClients(String account, List<ClientEntity> clients) {
        new ClientService(account, clients).send();
    }

    public static void addTransaction(String account, TransactionEntity transaction) {
        BTG360.addTransactions(account, Collections.singletonList(transaction));
    }

    public static void addTransactions(String account, List<TransactionEntity> transactions) {
        new TransactionService(account, transactions).send();
    }

    public static void addSearch(String account, SearchEntity search) {
        BTG360.addSearchs(account, Collections.singletonList(search));
    }

    public static void addSearchs(String account, List<SearchEntity> searchs) {
        new SearchService(account, searchs).send();
    }

    public static void addWishList(String account, WishListEntity wish) {
        BTG360.addWishList(account, Collections.singletonList(wish));
    }

    public static void addWishList(String account, List<WishListEntity> wishs) {
        new WishListService(account, wishs).send();
    }

    public static void addWarnMe(String account, WarnMeEntity warn) {
        BTG360.addWarnMe(account, Collections.singletonList(warn));
    }

    public static void addWarnMe(String account, List<WarnMeEntity> warns) {
        new WarnMeService(account, warns).send();
    }
}
