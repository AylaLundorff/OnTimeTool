package com.example.ayla.ontimetool;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Pop Alex-Cristian on 12/2/2015.
 */
public class StoreActivity extends AppCompatActivity {

    private ProductModel mProductModel;
    private TextView mAddressPlaceholder, mPhonePlaceholder, mCommercialPlaceholder;
    private ImageView mLogoPlaceholder;
    private Button mBack, mNewSearch, mNav;

    private static final String STARK = "stark";
    private static final String BAUHAUS = "bauhaus";
    private static final String HARALD = "harald";
    private static final String JEM_OG_FIG = "jem og fix";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chainresult);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(MainActivity.PARCELABLE_INTENT_KEY)) {
            mProductModel = bundle.getParcelable(MainActivity.PARCELABLE_INTENT_KEY);
        }

        mAddressPlaceholder = (TextView) findViewById(R.id.address_placeholder);
        mPhonePlaceholder = (TextView) findViewById(R.id.phone_placeholder);
        mCommercialPlaceholder = (TextView) findViewById(R.id.commercial_placeholder);
        mLogoPlaceholder = (ImageView) findViewById(R.id.store_logo);
        mLogoPlaceholder = (ImageView) findViewById(R.id.logo_placeholder);
        mBack = (Button) findViewById(R.id.back);
        mNewSearch = (Button) findViewById(R.id.newsearch);
        mNav = (Button) findViewById(R.id.nav);

        mNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=55.871617, 9.886026&daddr=" + mProductModel.address.latitude + "," + mProductModel.address.longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mNewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(StoreActivity.this, MainActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
            }
        });

        setStorePicture();
        mAddressPlaceholder.setText(mProductModel.address.street_name + " " + mProductModel.address.street_number + ", " + mProductModel.address.zip_code + " " + mProductModel.address.city_name);

        String text = "T. ";
        StringBuilder mStringBuilder = new StringBuilder(text);
        int phoneSpanStart = mStringBuilder.length();
        String mPhoneNumber = "" + mProductModel.address.phone_number;
        mStringBuilder.append(mPhoneNumber);
        int phoneEndSpan = mStringBuilder.length();

        ClickableSpan mClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mProductModel.address.phone_number));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(Color.BLUE);
            }
        };

        SpannableString mSpannableString = new SpannableString(mStringBuilder);
        mSpannableString.setSpan(mClickableSpan, phoneSpanStart, phoneEndSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mPhonePlaceholder.setText(mSpannableString);
        mPhonePlaceholder.setMovementMethod(LinkMovementMethod.getInstance());
        mCommercialPlaceholder.setText(mProductModel.commercial_text);
    }

    private void setStorePicture() {

        String uri;

        if (mProductModel.address.store_name.toLowerCase().contains(STARK)) {
            uri = "@drawable/stark_store";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            mLogoPlaceholder.setImageDrawable(res);
        }
        if (mProductModel.address.store_name.toLowerCase().contains(BAUHAUS)) {
            uri = "@drawable/bauhaus_store";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            mLogoPlaceholder.setImageDrawable(res);
        }
        if (mProductModel.address.store_name.toLowerCase().contains(HARALD)) {
            uri = "@drawable/harald_nyborg";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            mLogoPlaceholder.setImageDrawable(res);
        }
        if (mProductModel.address.store_name.toLowerCase().contains(JEM_OG_FIG)) {
            uri = "@drawable/jemogfix";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            mLogoPlaceholder.setImageDrawable(res);
        }
    }
}
