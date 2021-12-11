package smu.example.cabling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    ImageButton loginButton;
    Animation anim_FadeIn;
    Animation anim_ball;
    Animation anim_scale;
    ImageView logo;
    ImageView letter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getHashKey();

        logo = findViewById(R.id.logo);
        letter = findViewById(R.id.letter);

        anim_FadeIn= AnimationUtils.loadAnimation(this,R.anim.anim_splash_fadein);
        anim_ball=AnimationUtils.loadAnimation(this,R.anim.anim_splash_ball);
        anim_scale=AnimationUtils.loadAnimation(this,R.anim.anim_splash_scale);


        logo.startAnimation(anim_ball);
        letter.startAnimation(anim_scale);

        loginButton = findViewById(R.id.startbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_splash_fadein, R.anim.anim_splash_fadeout);
                finish();
            }
        });



        }
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try{
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        if(packageInfo == null)
            Log.e("HashKey", "HashKey:null");
        for(Signature signature : packageInfo.signatures){
            try{
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("HashKey", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e){
                Log.e("HashKey", "HashKey Error.signature=" + signature, e);
            }
        }

    }



    }






