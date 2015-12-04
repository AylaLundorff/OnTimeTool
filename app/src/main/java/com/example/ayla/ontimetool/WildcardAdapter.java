package com.example.ayla.ontimetool;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pop Alex-Cristian on 12/3/2015.
 */
public class WildcardAdapter extends BaseAdapter implements Filterable {

    private List<ProductModel> mProductModels;
    private Context mContext;
    private ImageView mProductView;

    public WildcardAdapter(List<ProductModel> productModels, Context context) {
        this.mProductModels = productModels;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mProductModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = View.inflate(mContext, R.layout.wildcard_item_adapter, null);
        TextView mPriceView = (TextView) mView.findViewById(R.id.price_placeholder);
        TextView mDescriptionView = (TextView) mView.findViewById(R.id.description_placeholder);
        TextView mDistanceView = (TextView) mView.findViewById(R.id.distance_placeholder);
        mProductView = (ImageView) mView.findViewById(R.id.item_pic_placeholder);

        ProductModel mProductModel = mProductModels.get(position);
        mPriceView.setText("Price : " + mProductModel.product_price + " " + mProductModel.price_currency);
        mDistanceView.setText("Location: " + mProductModel.distance / 1000 + " KM");
        mDescriptionView.setText(mProductModel.product_description);
        displayPicture(position);

        return mView;
    }

    private void displayPicture(int position) {
        String uri;
        switch (position) {
            case 0: {
                uri = "@drawable/screws";
                int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
                Drawable res = mContext.getResources().getDrawable(imageResource);
                mProductView.setImageDrawable(res);
                break;
            }

            case 1: {
                uri = "@drawable/screws2";
                int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
                Drawable res = mContext.getResources().getDrawable(imageResource);
                mProductView.setImageDrawable(res);
                break;
            }

            case 2: {
                uri = "@drawable/screws3";
                int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
                Drawable res = mContext.getResources().getDrawable(imageResource);
                mProductView.setImageDrawable(res);
                break;
            }

            case 3: {
                uri = "@drawable/screws4";
                int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
                Drawable res = mContext.getResources().getDrawable(imageResource);
                mProductView.setImageDrawable(res);
                break;
            }

            default:
                break;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint.length() == 0) {
                    results.values = mProductModels;
                    results.count = mProductModels.size();
                } else {
                    List<ProductModel> suggestions = new ArrayList<>();
                    for (ProductModel item : mProductModels) {
                        if (item.product_name.toLowerCase().contains(constraint.toString().toLowerCase()) || item.product_description.toLowerCase().contains(constraint.toString().toLowerCase()))
                            suggestions.add(item);
                    }

                    results.values = suggestions;
                    results.count = suggestions.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    mProductModels = (List<ProductModel>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }
}
