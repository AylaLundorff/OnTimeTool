package com.example.ayla.ontimetool;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import java.util.logging.SocketHandler;

public class MainActivity extends AppCompatActivity {

    private EditText mDbEditText, mEanEditText;
    private AutoCompleteTextView mAutoCompleteTextView;
    private WildcardAdapter mWildcardAdapter;
    private ImageView mLogo;
    private Button mButtonScan;

    public final static String DB_TUN_INTENT_KEY = "dbNumber";
    public final static String EAN_INTENT_KEY = "eanNumber";
    public final static String PARCELABLE_INTENT_KEY = "parcelableObj";
    public final static String FIRST_TIME = "isFirstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDb();

        boolean isFirstTime = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(FIRST_TIME, true);
        if (isFirstTime) {
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(FIRST_TIME, false).commit();
            populateDb();
        }

        mDbEditText = (EditText) findViewById(R.id.db);
        mEanEditText = (EditText) findViewById(R.id.ean);
        mButtonScan = (Button) findViewById(R.id.buttonscan);
        mButtonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
            }
        });
        mLogo = (ImageView) findViewById(R.id.logo);
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.ontimetool.dk"));
                startActivity(i);
            }
        });
        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_text_view);
        mWildcardAdapter = new WildcardAdapter(DatabaseHelper.getInstance().getAll(), getApplicationContext());
        mAutoCompleteTextView.setAdapter(mWildcardAdapter);
        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWildcardAdapter.getFilter().filter(s);
                mWildcardAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(MainActivity.this, SearchResultActivity.class);
                ProductModel mProductModel = (ProductModel) parent.getItemAtPosition(position);
                mIntent.putExtra(MainActivity.EAN_INTENT_KEY, mProductModel.ean_number);
                startActivity(mIntent);

                mAutoCompleteTextView.getText().clear();
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return true;
                }
                return true;
            }
        });

        mEanEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ean_error), Toast.LENGTH_SHORT);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(30);
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    try {
                        if (mEanEditText.length() != 13) {
                            toast.show();
                        } else {
                            intent.putExtra(EAN_INTENT_KEY, Long.parseLong(mEanEditText.getText().toString()));
                            startActivity(intent);
                        }
                    } catch (NumberFormatException ex) {
                        toast.show();
                    }

                }
                return true;
            }
        });
        mDbEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.tun_error), Toast.LENGTH_SHORT);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(30);
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    try {
                        if (mDbEditText.length() != 7) {
                            toast.show();
                        } else {
                            intent.putExtra(DB_TUN_INTENT_KEY, Integer.parseInt(mDbEditText.getText().toString()));
                            startActivity(intent);
                        }
                    } catch (NumberFormatException ex) {
                        toast.show();
                    }
                }
                return true;
            }
        });
    }

    private void initializeDb() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        configurationBuilder.addModelClasses();
        ActiveAndroid.initialize(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            Toast.makeText(MainActivity.this, scanResult.getContents(), Toast.LENGTH_SHORT).show();
            Intent mIntent = new Intent(MainActivity.this, SearchResultActivity.class);
            mIntent.putExtra(EAN_INTENT_KEY, Long.parseLong(scanResult.getContents()));
            startActivity(mIntent);
        }
    }

    private void populateDb() {
        Address mAddress = new Address();
        mAddress.store_name = "STARK";
        mAddress.street_name = "Strandpromenaden";
        mAddress.street_number = 4;
        mAddress.zip_code = 8700;
        mAddress.city_name = "Horsens";
        mAddress.phone_number = 76253000;
        mAddress.latitude = 55.8601969;
        mAddress.longitude = 9.869981199999984;
        mAddress.save();

        Address mAddress1 = new Address();
        mAddress1.store_name = "jem og fix Horsens";
        mAddress1.street_name = "Høegh Guldbergs Gade";
        mAddress1.street_number = 21;
        mAddress1.zip_code = 8700;
        mAddress1.city_name = "Horsens";
        mAddress1.phone_number = 76415520;
        mAddress1.latitude = 55.852347;
        mAddress1.longitude = 9.849292999999989;
        mAddress1.save();

        Address mAddress2 = new Address();
        mAddress2.store_name = "Harald Nyborg";
        mAddress2.street_name = "Høegh Guldbergs Gade";
        mAddress2.street_number = 15;
        mAddress2.zip_code = 8700;
        mAddress2.city_name = "Horsens";
        mAddress2.phone_number = 63959616;
        mAddress2.latitude = 55.853866;
        mAddress2.longitude = 9.85050799999999;
        mAddress2.save();

        Address mAddress3 = new Address();
        mAddress3.store_name = "Bauhaus";
        mAddress3.street_name = "Anelystparken 16";
        mAddress3.street_number = 15;
        mAddress3.zip_code = 8381;
        mAddress3.city_name = "Aarhus";
        mAddress3.phone_number = 89449100;
        mAddress3.latitude = 56.18366899999999;
        mAddress3.longitude = 10.100036000000046;
        mAddress3.save();

        ProductModel mProductModel = new ProductModel();
        mProductModel.product_name = "screws";
        mProductModel.ean_number = 5701291370555l;
        mProductModel.tun_number = 5126333;
        mProductModel.product_description = "NKT Basic Screw with Ruspert 1000 surface. TX20 5.0 x 100/55 mm. 200 pcs";
        mProductModel.product_price = 109.95f;
        mProductModel.price_currency = "DKK";
        mProductModel.amount = 10;
        mProductModel.address = mAddress;
        mProductModel.commercial_text = "Become DIY member - for free!";
        mProductModel.distance = 2300;
        mAddress3.latitude = 56.18366899999999;
        mProductModel.save();

        ProductModel mProductModel1 = new ProductModel();
        mProductModel1.product_name = "screws";
        mProductModel1.ean_number = 5701291148468l;
        mProductModel1.tun_number = 3416096;
        mProductModel1.product_description = "SPUN + stainless steel screws. Kv. A4, 5,0 x 100/55 mm, 200 stk.";
        mProductModel1.product_price = 99.95f;
        mProductModel1.price_currency = "DKK";
        mProductModel1.amount = 7;
        mProductModel1.address = mAddress1;
        mProductModel1.commercial_text = "The customer card gives you up to 40 000 DKK to shop for, and it only takes 15 minutes to get the card";
        mProductModel1.distance = 4100;
        mProductModel1.save();

        ProductModel mProductModel2 = new ProductModel();
        mProductModel2.product_name = "screws";
        mProductModel2.ean_number = 5701291148468l;
        mProductModel2.tun_number = 3416096;
        mProductModel2.product_description = "SPUN + stainless steel screws. Kv. A4, 5,0 x 100/55 mm, 200 stk.";
        mProductModel2.product_price = 89.95f;
        mProductModel2.price_currency = "DKK";
        mProductModel2.amount = 6;
        mProductModel2.address = mAddress2;
        mProductModel2.commercial_text = "Harald Nyborg’s customer card - provides card to your dreams and credit to more";
        mProductModel2.distance = 3900;
        mProductModel2.save();

        ProductModel mProductModel3 = new ProductModel();
        mProductModel3.product_name = "screws";
        mProductModel3.ean_number = 5701291370555l;
        mProductModel3.tun_number = 5126333;
        mProductModel3.product_description = "NKT Basic Screw with Ruspert 1000 surface. TX20 5.0 x 100/55 mm. 200 pcs.";
        mProductModel3.product_price = 99.95f;
        mProductModel3.price_currency = "DKK";
        mProductModel3.amount = 15;
        mProductModel3.address = mAddress3;
        mProductModel3.commercial_text = "BAUHAUS plus card private - Get the last piece to your dream!";
        mProductModel3.distance = 39200;
        mProductModel3.save();
    }
}
