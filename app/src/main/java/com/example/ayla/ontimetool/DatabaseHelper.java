package com.example.ayla.ontimetool;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Pop Alex-Cristian on 11/30/2015.
 */
public class DatabaseHelper {

    private static String TAG = DatabaseHelper.class.getSimpleName();
    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance() {
        if (instance == null) instance = getSync();
        return instance;
    }

    private static synchronized DatabaseHelper getSync() {
        if (instance == null) instance = new DatabaseHelper();
        return instance;
    }

    public <T extends Model> T getObject(Class<T> type, String key, Object value) {
        return new Select().from(type).where(key + " = ?", value).executeSingle();
    }

    public <T extends Model> List<T> getManyObjects(Class<T> type, String key, Object value) {
        return new Select().from(type).where(key + " = ?", value).execute();
    }

    public List<ProductModel> getAll() {
        return new Select().from(ProductModel.class).execute();
    }

    // Get products using DB/TUN
    public List<ProductModel> getProductsByPrice(int db_number) {
        return new Select().from(ProductModel.class).where("tun_number = ?", db_number).orderBy("product_price ASC").execute();
    }

    public List<ProductModel> getProductsByLocation(int db_number) {
        return new Select().from(ProductModel.class).where("tun_number = ?", db_number).orderBy("distance ASC").execute();
    }

    public List<ProductModel> getProductsByStock(int db_number) {
        return new Select().from(ProductModel.class).where("tun_number = ?", db_number).orderBy("amount DESC").execute();
    }

    public List<ProductModel> getProductsByDbNumber(int db_number) {
        return new Select().from(ProductModel.class).where("tun_number = ?", db_number).execute();
    }

    // Get products using EAN number
    public List<ProductModel> getProductsByEanNumber(long ean_number) {
        return new Select().from(ProductModel.class).where("ean_number = ?", ean_number).execute();
    }

    public List<ProductModel> getProductsByPrice(long ean_number) {
        return new Select().from(ProductModel.class).where("ean_number = ?", ean_number).orderBy("product_price ASC").execute();
    }

    public List<ProductModel> getProductsByLocation(long ean_number) {
        return new Select().from(ProductModel.class).where("ean_number = ?", ean_number).orderBy("distance ASC").execute();
    }

    public List<ProductModel> getProductsByStock(long ean_number) {
        return new Select().from(ProductModel.class).where("ean_number = ?", ean_number).orderBy("amount DESC").execute();
    }
}
