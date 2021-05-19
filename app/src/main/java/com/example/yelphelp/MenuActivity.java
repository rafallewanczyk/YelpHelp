package com.example.yelphelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yelphelp.Entrance.ChooseLoginRegistrationActivity;
import com.example.yelphelp.Offer.CreateOfferActivity;
import com.example.yelphelp.Offer.OfferListActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    private Button mCreateOfferButton, mMyOffersButton, mBrowseOffersButton, mSettingsButton, mSignOutButton, mBelongingOffersButton, mEditOffersButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();
        mCreateOfferButton = (Button) findViewById(R.id.createOfferButton);
        mCreateOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CreateOfferActivity.class);
                startActivity(intent);
            }
        });
        mMyOffersButton = (Button) findViewById(R.id.myOffersButton);
        mMyOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, OfferListActivity.class);
                Bundle b = new Bundle();
                b.putString("offersType", "acceptedOffers");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        mBelongingOffersButton = (Button) findViewById(R.id.belongingOffers);
        mBelongingOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, OfferListActivity.class);
                Bundle b = new Bundle();
                b.putString("offersType", "belongingOffers");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        mEditOffersButton = (Button) findViewById(R.id.editOffers);
        mEditOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, OfferListActivity.class);
                Bundle b = new Bundle();
                b.putString("offersType", "editOffers");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        mBrowseOffersButton = (Button) findViewById(R.id.browseOffersButton);
        mBrowseOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, BrowseOffersActivity.class);
                startActivity(intent);
            }
        });
        mSettingsButton = (Button) findViewById(R.id.settingsButton);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


        mSignOutButton = (Button) findViewById(R.id.signOutButton);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutUser(v);
            }
        });
    }

    public void signOutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MenuActivity.this, ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}