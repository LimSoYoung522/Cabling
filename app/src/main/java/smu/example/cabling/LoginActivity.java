package smu.example.cabling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity{

    public String userid, userpw;

    ImageButton btn_login, btn_regist;
    EditText login_id;
    EditText login_pw;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("로그인");

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String emailfromR = intent.getStringExtra("email");
        String pwfromR = intent.getStringExtra("pw");

        login_id = (EditText)findViewById(R.id.edit_id);
        login_pw = (EditText)findViewById(R.id.edit_pw);
        login_id.setText(emailfromR);
        login_pw.setText(pwfromR);

        btn_login = (ImageButton)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = login_id.getText().toString().trim();
                userpw = login_pw.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(userid, userpw)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    StyleableToast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT, R.style.mytoast).show();
                                    Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                                    startActivity(intent);
                                }else{
                                    StyleableToast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT, R.style.mytoast).show();
                                }
                            }
                        });
            }
        });

        btn_regist = (ImageButton)findViewById(R.id.btn_register);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}

