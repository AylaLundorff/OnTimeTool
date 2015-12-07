package com.example.ayla.ontimetool;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private AutoCompleteTextView mFilterEditText;

    private WildcardAdapter mWildcardAdapter;
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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        setWildcardAdapter();

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
        mFilterEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel mProductModel = (ProductModel) parent.getItemAtPosition(position);
                eanNumber = mProductModel.ean_number;
                mProductModels = DatabaseHelper.getInstance().getProductsByEanNumber(eanNumber);
                mResultAdapter.clear();
                mResultAdapter.addList(mProductModels);
                mFilterEditText.getText().clear();
            }
        });

        mFilterEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return true;
                }
                return true;
            }
        });
    }

    private void initializeViews() {
        mListView = (ListView) findViewById(R.id.result_list);
        mFilterEditText = (AutoCompleteTextView) findViewById(R.id.wildcard_text_filter);
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
                break;
            }
            case R.id.logo: {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.ontimetool.dk"));
                startActivity(i);
                break;
            }
            default:
                break;
        }
    }

    private void setWildcardAdapter() {
        List<ProductModel> mProductModels = DatabaseHelper.getInstance().getAll();
        mWildcardAdapter = new WildcardAdapter(mProductModels, getApplicationContext());
        mFilterEditText.setAdapter(mWildcardAdapter);

    }
}
