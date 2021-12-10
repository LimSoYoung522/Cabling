package smu.example.cabling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class UserActivity extends AppCompatActivity {

    public static Context context_user;
    public String username;
    public int point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        context_user = this;
        username = ((LoginActivity)LoginActivity.context_login).username;

        point = 0;


    }
}