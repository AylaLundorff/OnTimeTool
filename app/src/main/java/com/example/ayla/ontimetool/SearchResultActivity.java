package com.example.ayla.ontimetool;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pop Alex-Cristian on 12/2/2015.
 */
public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private EditText mFilterEditText;
    private ResultAdapter mResultAdapter;

    private int dbNumber;
    private long eanNumber;

    private Button mPrice, mDist, mStock, mBack;
    private List<ProductModel> mProductModels;
    private ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);

        initializeViews();

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null)
            if (mBundle.containsKey(MainActivity.DB_TUN_INTENT_KEY)) {
                dbNumber = mBundle.getInt(MainActivity.DB_TUN_INTENT_KEY);
                mProductModels = DatabaseHelper.getInstance().getProductsByDbNumber(dbNumber);
            } else if (mBundle.containsKey(MainActivity.EAN_INTENT_KEY)) {
                eanNumber = mBundle.getLong(MainActivity.EAN_INTENT_KEY);
                mProductModels = DatabaseHelper.getInstance().getProductsByEanNumber(eanNumber);
            }

        mResultAdapter = new ResultAdapter(mProductModels, SearchResultActivity.this);
        mListView.setAdapter(mResultAdapter);


        mFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mResultAdapter.getFilter().filter(s);
                mResultAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initializeViews() {
        mListView = (ListView) findViewById(R.id.result_list);
        mFilterEditText = (EditText) findViewById(R.id.wildcard_text_filter);
        mPrice = (Button) findViewById(R.id.price);
        mDist = (Button) findViewById(R.id.dist);
        mStock = (Button) findViewById(R.id.stock);
        mBack = (Button) findViewById(R.id.back);
        mLogo = (ImageView) findViewById(R.id.logo);
        mPrice.setOnClickListener(this);
        mLogo.setOnClickListener(this);
        mDist.setOnClickListener(this);
        mStock.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.price: {
                List<ProductModel> price_models = new ArrayList<>();
                mResultAdapter.clear();
                if (dbNumber == 0) {
                    price_models = DatabaseHelper.getInstance().getProductsByPrice(eanNumber);
                } else if (eanNumber == 0) {
                    price_models = DatabaseHelper.getInstance().getProductsByPrice(dbNumber);
                }
                mResultAdapter.addList(price_models);
                break;
            }
            case R.id.dist: {
                List<ProductModel> price_models = new ArrayList<>();
                mResultAdapter.clear();
                if (dbNumber == 0) {
                    price_models = DatabaseHelper.getInstance().getProductsByLocation(eanNumber);
                } else if (eanNumber == 0) {
                    price_models = DatabaseHelper.getInstance().getProductsByLocation(dbNumber);
                }
                mResultAdapter.addList(price_models);
                break;
            }
            case R.id.stock: {
                List<ProductModel> price_models = new ArrayList<>();
                mResultAdapter.clear();
                if (dbNumber == 0) {
                    price_models = DatabaseHelper.getInstance().getProductsByStock(eanNumber);
                } else if (eanNumber == 0) {
                    price_models = DatabaseHelper.getInstance().getProductsByStock(dbNumber);
                }
                mResultAdapter.addList(price_models);
                break;
            }
            case R.id.back: {
                onBackPressed();
            }
            case R.id.logo: {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.ontimetool.dk"));
                startActivity(i);

            }
            default:
                break;
        }
    }
}
