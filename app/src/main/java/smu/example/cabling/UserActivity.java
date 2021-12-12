package smu.example.cabling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.net.HttpURLConnection;

import io.github.muddz.styleabletoast.StyleableToast;

public class UserActivity extends AppCompatActivity {

    public String username1;
    public static int point1;

    ImageView iv;
    TextView tvpoint, tvname;
    ImageButton btpoint;
    ImageButton home;
    RadioGroup rgpoint;
    RadioButton p1, p2, p3;
    Intent intent;

    private final int GALLERY_CODE = 10;
    public String filename;
    int num=0;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mContext = this;

        iv = (ImageView) findViewById(R.id.imageView);
        tvpoint = (TextView) findViewById(R.id.tvpoint);
        btpoint = (ImageButton) findViewById(R.id.pointbutton);
        home = (ImageButton)  findViewById(R.id.Home);
        rgpoint = (RadioGroup) findViewById(R.id.rgpoint);
        p1 = (RadioButton) findViewById(R.id.p1);
        p2 = (RadioButton) findViewById(R.id.p2);
        p3 = (RadioButton) findViewById(R.id.p3);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){ username1 = user.getDisplayName(); }

        readUsers(0);
        tvname = (TextView) findViewById(R.id.name);
        tvname.setText("안녕하세요,\n" + username1 + " 님");

        btpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p1.isChecked()) point1 += 100;
                else if(p2.isChecked()) point1 += 500;
                else if(p3.isChecked()) point1 += 1000;

                readUsers(1);
                tvpoint.setText("현재 잔여 포인트는 " + Integer.toString(point1) + " point 입니다.");
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, DrawerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAlbum();
            }
        });
    }

    private void loadAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // 사진 업로드
        if(requestCode == GALLERY_CODE){
            Uri file = data.getData();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            filename = "profile" + (num++) + ".jpg";

            Log.d("uri", String.valueOf(file));

            StorageReference riversRef = storageRef.child("profile_img/" + filename);
            UploadTask uploadTask = riversRef.putFile(file);

            try{
                /*InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();*/
                //iv.setImageBitmap(img);
                readUsers(1);
            }catch(Exception e){
                e.printStackTrace();
            }

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    StyleableToast.makeText(UserActivity.this, "오류로 인해 프로필 사진을 변경할 수 없습니다.", Toast.LENGTH_SHORT, R.style.mytoast).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    changeurl();
                    StyleableToast.makeText(UserActivity.this, "프로필 사진을 변경하였습니다.", Toast.LENGTH_SHORT, R.style.mytoast).show();
                }
            });
        }
    }
    public void readUsers(int i){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        String key = snapshot.getKey();
                        User value = snapshot.getValue(User.class);

                        if(value.username.equals(username1)){
                            if(i == 0){
                                username1 = value.username;
                                point1 = value.point;
                                tvpoint.setText("현재 잔여 포인트는 " + value.point + " point 입니다.");
                            }else{
                                reference.child(key).child("point").setValue(point1);
                            }

                            Uri file = Uri.parse(value.photo);
                            Log.d("photoURI", String.valueOf(file));

                            try{
                                Glide.with(getApplicationContext()).load(file).into(iv);
                                //Bitmap img = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), file));
                                //iv.setImageBitmap(img);
                                Log.d("photoURIsuccess", "1");
                            }catch(Exception e){
                                e.printStackTrace();
                                Log.d("photoURIfail", "1");
                            }

                            break;
                        }

                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void changeurl(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("profile_img/" + filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                String key = snapshot.getKey();
                                User value = snapshot.getValue(User.class);

                                if(value.username.equals(username1)){
                                    Log.d("URI photo", String.valueOf(uri));
                                    reference.child(key).child("photo").setValue(String.valueOf(uri));
                                    break;
                                }

                            } catch (ClassCastException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}