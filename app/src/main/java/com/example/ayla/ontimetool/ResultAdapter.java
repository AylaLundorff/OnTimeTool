package com.example.ayla.ontimetool;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends BaseAdapter implements Filterable {

    private List<ProductModel> mProductModels;
    private Context mContext;

    public ResultAdapter(List<ProductModel> productModels, Context context) {
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
        View mView = View.inflate(mContext, R.layout.adapter_item, null);
        TextView product_description_text = (TextView) mView.findViewById(R.id.product_description_text);
        TextView price_distance_stock_text = (TextView) mView.findViewById(R.id.price_distance_stock_text);
        Button store_button = (Button) mView.findViewById(R.id.store_button);

        final ProductModel mProductModel = mProductModels.get(position);

        product_description_text.setText(mProductModel.product_description);
        price_distance_stock_text.setText(mProductModel.product_price + " " + mProductModel.price_currency + "\n" + mProductModel.distance / 1000 + " km" + "\n" + mProductModel.amount + " boxes");

        store_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, StoreActivity.class);
                mIntent.putExtra(MainActivity.PARCELABLE_INTENT_KEY, mProductModel);
                mContext.startActivity(mIntent);
            }
        });

        return mView;
    }

    public void clear() {
        mProductModels.clear();
    }

    public void addList(List<ProductModel> models) {
        this.mProductModels = models;
        notifyDataSetChanged();
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
                        if (item.product_name.toLowerCase().contains(constraint.toString().toLowerCase()))
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
