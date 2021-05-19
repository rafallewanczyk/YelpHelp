package com.example.yelphelp.Offer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.yelphelp.MapsActivity;
import com.example.yelphelp.R;

public class ShowOfferActivity extends EditOfferActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOfferInfo();

        mTitleEdit.setVisibility(View.GONE);
        mDescriptionEdit.setVisibility(View.GONE);

        mTitleView.setVisibility(View.VISIBLE);
        mDescriptionView.setVisibility(View.VISIBLE);

        mOfferImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        mBack.setVisibility(View.GONE);
        mConfirm.setText("Back");
        mMap.setText("Show Location");

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowOfferActivity.this, MapsActivity.class);
                Bundle b = new Bundle();
                b.putDouble("lat", lat);
                b.putDouble("lang", lang);
                b.putBoolean("editable", false);
                intent.putExtras(b);
                startActivityForResult(intent,66);
            }
        });
    }

}