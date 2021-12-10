package smu.example.cabling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    public String username;
    public static int point;

    TextView tvpoint, tvname;
    Button btpoint;
    RadioGroup rgpoint;
    RadioButton p1, p2, p3;

    SharedPreferences pointp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){ username = user.getDisplayName(); }

        tvname = (TextView) findViewById(R.id.name);
        tvname.setText(username);

        tvpoint = (TextView) findViewById(R.id.tvpoint);
        btpoint = (Button) findViewById(R.id.pointbutton);
        rgpoint = (RadioGroup) findViewById(R.id.rgpoint);
        p1 = (RadioButton) findViewById(R.id.p1);
        p2 = (RadioButton) findViewById(R.id.p2);
        p3 = (RadioButton) findViewById(R.id.p3);

        tvpoint.setText(username + "님이 보유하신 포인트는 \n" + Integer.toString(point) + "point 입니다.");

        btpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p1.isChecked()) point += 100;
                else if(p2.isChecked()) point += 500;
                else if(p3.isChecked()) point += 1000;

                tvpoint.setText(username + "님이 보유하신 포인트는 \n" + Integer.toString(point) + "point 입니다.");
            }
        });
    }
}