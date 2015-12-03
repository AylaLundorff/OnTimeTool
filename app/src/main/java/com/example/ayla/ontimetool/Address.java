package com.example.ayla.ontimetool;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Pop Alex-Cristian on 11/30/2015.
 */
@Table(name = "Address")
public class Address extends Model implements Parcelable {

    public Address() {
        super();
    }

    @Column
    public String store_name;
    @Column
    public String street_name;
    @Column
    public int street_number;
    @Column
    public int zip_code;
    @Column
    public String city_name;
    @Column
    public int phone_number;
    @Column
    public double latitude;
    @Column
    public double longitude;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.store_name);
        dest.writeString(this.street_name);
        dest.writeInt(this.street_number);
        dest.writeInt(this.zip_code);
        dest.writeString(this.city_name);
        dest.writeInt(this.phone_number);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected Address(Parcel in) {
        this.store_name = in.readString();
        this.street_name = in.readString();
        this.street_number = in.readInt();
        this.zip_code = in.readInt();
        this.city_name = in.readString();
        this.phone_number = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
