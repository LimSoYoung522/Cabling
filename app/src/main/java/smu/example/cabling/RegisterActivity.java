package smu.example.cabling;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.muddz.styleabletoast.StyleableToast;

public class RegisterActivity extends AppCompatActivity {

    EditText login_name, login_id, login_pw;
    String username, userid, userpw;
    ImageButton btn_register;
    FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("회원가입");

        mAuth = FirebaseAuth.getInstance();

        login_name = (EditText)findViewById(R.id.edit_name);
        login_id = (EditText)findViewById(R.id.edit_id);
        login_pw = (EditText)findViewById(R.id.edit_pw);

        btn_register = (ImageButton)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = login_name.getText().toString().trim();
                userid = login_id.getText().toString().trim();
                userpw = login_pw.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(userid, userpw)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    createUser(username, userid);
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("Profile", "User profile updated.");
                                                    }
                                                }
                                            });
                                    updateUI(user);
                                    StyleableToast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다. 로그인해주세요.", Toast.LENGTH_SHORT, R.style.mytoast).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra("email", userid);
                                    intent.putExtra("pw", userpw);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    updateUI(null);
                                    StyleableToast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT, R.style.mytoast).show();
                                    return;
                                }
                            }
                        });
            }
        });

    }

    public void createUser(String username, String useremail){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference newReference = reference.push();

        User user = new User(username, useremail, 0, "https://firebasestorage.googleapis.com/v0/b/cabling-hjwlsy.appspot.com/o/profile_img%2Fuser.jpeg?alt=media&token=f01ac1c3-324a-46b5-8688-6ad760f49b39");

        newReference.setValue(user);
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                String newUserKey = newReference.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) reload();
    }

    private void reload(){

    }
    private void updateUI(FirebaseUser user){

    }
}
