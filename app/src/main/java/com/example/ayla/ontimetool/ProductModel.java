package com.example.ayla.ontimetool;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Products")
public class ProductModel extends Model implements Parcelable {

    public ProductModel() {
        super();
    }

    @Column
    public String product_name;
    @Column
    public long ean_number;
    @Column
    public int tun_number;
    @Column
    public String product_description;
    @Column
    public float product_price;
    @Column
    public String price_currency;
    @Column
    public int amount;
    @Column(name = "Address", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Address address;
    @Column
    public String commercial_text;
    @Column
    public long distance;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_name);
        dest.writeLong(this.ean_number);
        dest.writeInt(this.tun_number);
        dest.writeString(this.product_description);
        dest.writeFloat(this.product_price);
        dest.writeString(this.price_currency);
        dest.writeInt(this.amount);
        dest.writeParcelable(this.address, 0);
        dest.writeString(this.commercial_text);
        dest.writeLong(this.distance);
    }

    protected ProductModel(Parcel in) {
        this.product_name = in.readString();
        this.ean_number = in.readLong();
        this.tun_number = in.readInt();
        this.product_description = in.readString();
        this.product_price = in.readFloat();
        this.price_currency = in.readString();
        this.amount = in.readInt();
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.commercial_text = in.readString();
        this.distance = in.readLong();
    }

    public static final Parcelable.Creator<ProductModel> CREATOR = new Parcelable.Creator<ProductModel>() {
        public ProductModel createFromParcel(Parcel source) {
            return new ProductModel(source);
        }

        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };
}

