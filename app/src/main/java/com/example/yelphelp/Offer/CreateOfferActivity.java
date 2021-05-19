package com.example.yelphelp.Offer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yelphelp.MapsActivity;
import com.example.yelphelp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateOfferActivity extends AppCompatActivity {

    protected Button mBack, mConfirm, mMap;
    protected EditText mDescriptionEdit, mTitleEdit;
    protected TextView mTitleView, mDescriptionView;
    protected ImageView mOfferImage;

    protected FirebaseAuth mAuth;
    protected DatabaseReference mUserDb, mOfferDb;
    protected String userId, description, title, offerImageUrl;

    protected Uri resultUri;

    protected String offerId;
    protected double lat, lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lat = 52.21644521012581 ;
        lang = 21.016039364039898;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);

        offerId = "";
        mDescriptionEdit = (EditText) findViewById(R.id.description);
        mTitleEdit = (EditText) findViewById(R.id.title);
        mOfferImage = (ImageView) findViewById(R.id.offerImage);
        mTitleView = (TextView) findViewById(R.id.titleView);
        mDescriptionView = (TextView) findViewById(R.id.descriptionView);

        mBack = (Button) findViewById(R.id.back);
        mConfirm = (Button) findViewById(R.id.confirm);
        mMap = (Button) findViewById(R.id.map);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mOfferDb = FirebaseDatabase.getInstance().getReference().child("Offers");

        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOfferActivity.this, MapsActivity.class);
                Bundle b = new Bundle();
                b.putDouble("lat", lat);
                b.putDouble("lang", lang);
                b.putBoolean("editable", true);
                intent.putExtras(b);
                startActivityForResult(intent,66);
            }
        });


        mOfferImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOffer();
                finish();
                return;
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    protected void saveOffer() {
        title = mTitleEdit.getText().toString();
        description = mDescriptionEdit.getText().toString();

        Map offerInfo = new HashMap();
        offerInfo.put("title", title);
        offerInfo.put("description", description);
        offerInfo.put("owner", userId);
        offerInfo.put("offerImage", offerImageUrl);
        offerInfo.put("lat", lat);
        offerInfo.put("lang", lang);
        if (offerId.isEmpty()) {
            mOfferDb = mOfferDb.push();
            offerId = mOfferDb.getKey();
            mOfferDb.setValue(offerInfo);
        } else {
            mOfferDb = mOfferDb.child(offerId);
            mOfferDb.child("title").setValue(title);
            mOfferDb.child("description").setValue(description);
            mOfferDb.child("offerImage").setValue(offerImageUrl);
            mOfferDb.child("lat").setValue(lat);
            mOfferDb.child("lang").setValue(lang);
        }



        if (resultUri != null) {
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("offerImages").child(offerId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, boas);
            byte[] data = boas.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(e -> {
                finish();
            });

            uploadTask.addOnSuccessListener(taskSnapshot -> uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();

                }
                // Continue with the task to get the download URL
                return filepath.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String downloadUrl = task.getResult().toString();
                    Map offerInfo1 = new HashMap();
                    offerInfo1.put("offerImage", downloadUrl);
                    mOfferDb.updateChildren(offerInfo1);
                    finish();
                    return;

                }
            })).addOnFailureListener(e -> {
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mOfferImage.setImageURI(resultUri);


        } else if (requestCode == 66 && resultCode == RESULT_OK){
            lat = data.getExtras().getDouble("lat");
            lang = data.getExtras().getDouble("lang");
        }else {
            finish();
        }
    }
}