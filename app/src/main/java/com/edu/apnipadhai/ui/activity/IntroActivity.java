package com.edu.apnipadhai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

import com.edu.apnipadhai.R;
import com.google.firebase.auth.FirebaseAuth;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(false);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        .image(R.drawable.img_material_intro)//img_office)
                        .title(getString(R.string.intro_text_1))
                        .description(getString(R.string.intro_text_11))
                        .build()
               /*, new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage(getString(R.string.intro_text_2));
                    }
                }, "Work with love")*/);

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.second_slide_background)
                .buttonsColor(R.color.second_slide_buttons)
                .image(R.drawable.img_material_intro)
                .title(getString(R.string.intro_text_2))
                .description(getString(R.string.intro_text_22))
                .build());

        // addSlide(new CustomSlide());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.third_slide_background)
                .buttonsColor(R.color.third_slide_buttons)
                //.possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                //.neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.img_material_intro)//img_equipment)
                .title(getString(R.string.intro_text_3))
                .description(getString(R.string.intro_text_33))
                .build()/*,
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Try us!");
                    }
                }, "Tools")*/);

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.fourth_slide_background)
                .image(R.drawable.img_material_intro)
                .buttonsColor(R.color.fourth_slide_buttons)
                .title(getString(R.string.intro_text_4))
                .description(getString(R.string.intro_text_44))
                .build());
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Intent intent;
        if (null != FirebaseAuth.getInstance().getCurrentUser()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //Toast.makeText(this, "Try this library in your project! :)", Toast.LENGTH_SHORT).show();
    }
}