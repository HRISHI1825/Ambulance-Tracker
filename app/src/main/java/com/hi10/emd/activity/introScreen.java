package com.hi10.emd.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.hi10.emd.R;
import com.hi10.emd.helper.SessionManager;
import com.hi10.emd.helper.Utils;

import java.util.ArrayList;
import java.util.List;

public class introScreen extends AhoyOnboarderActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_intro_screen);
        mContext = this;

        if (SessionManager.getInstance(this).isLogin()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Register", "Register for a helpful medical application.", R.drawable.ambulance);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Locate  Hospital", " Searching for a hospital is now easy.", R.drawable.ambulance);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Blood Donor", " Find blood donors when you are in need.", R.drawable.ambulance);
        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("Health record", "Have your medical history handy.", R.drawable.ambulance);
        AhoyOnboarderCard ahoyOnboarderCard5 = new AhoyOnboarderCard("Pill Reminder", "We won't let you forget your pills..", R.drawable.ambulance);

        ahoyOnboarderCard1.setBackgroundColor(R.color.colorPrimary);
        ahoyOnboarderCard2.setBackgroundColor(R.color.colorPrimary);
        ahoyOnboarderCard3.setBackgroundColor(R.color.colorPrimary);
        ahoyOnboarderCard4.setBackgroundColor(R.color.colorPrimary);
        ahoyOnboarderCard5.setBackgroundColor(R.color.colorPrimary);


        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);
        pages.add(ahoyOnboarderCard5);


        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
            //page.setTitleTextSize(dpToPixels(12, this));
            //page.setDescriptionTextSize(dpToPixels(8, this));
            //page.setIconLayoutParams(width, height, marginTop, marginLeft, marginRight, marginBottom);
        }

        setFinishButtonTitle("Finish");
        showNavigationControls(true);
        //setGradientBackground();

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
        }

        //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        //setFont(face);

        setOnboardPages(pages);

    }

    @Override
    public void onFinishButtonPressed() {

        SharedPreferences preferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isWalkthroughShown",true);
        editor.commit();
        finish();
        Intent Login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(Login);
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
