package smu.example.cabling;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.muddz.styleabletoast.StyleableToast;

import static smu.example.cabling.UserActivity.point1;

public class AppointmentActivity extends AppCompatActivity {
    private static final String TAG = "map";
    private DatabaseReference mDatabase;
    Button seat[] = new Button[10];
    int[] seatID = {
            R.id.seat0, R.id.seat1, R.id.seat2,
            R.id.seat3, R.id.seat4, R.id.seat5,
            R.id.seat6, R.id.seat7, R.id.seat8,
            R.id.seat9
    };
    String result = "";
    int i;
    String cafe = "cafe";
    protected int num1;
    protected int num2;
    Context mContext;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent receive = getIntent();
        num1 = receive.getIntExtra("cafe_num", 0);

        if (num1 == 1) {
            setContentView(R.layout.activity_appoint1);
            setTitle("CAFE 1 예약화면");
        } else if (num1 == 2) {
            setContentView(R.layout.activity_appoint2);
            setTitle("CAFE 2 예약화면");
        } else if (num1 == 3) {
            setContentView(R.layout.activity_appoint3);
            setTitle("CAFE 3 예약화면");
        }

        mContext = getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        for (i = 0; i < 10; i++) {
            seat[i] = (Button) findViewById(seatID[i]);
            seat[i].setOnClickListener(new btnSeatListener(num1, i));
            seat[i].setBackground(ContextCompat.getDrawable(mContext, R.drawable.seat_o));
            setbg(i);
        }

        Button refresh = (Button)findViewById(R.id.refresh);
        refresh.setBackground(ContextCompat.getDrawable(this, R.drawable.refresh));
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(i = 0; i < 10; i++ ) setbg(i);
            }
        });

    }

    public void setbg(int num){
        mDatabase.child("CAFE").child(cafe.concat(String.valueOf(num1))).child("seat").child(String.valueOf(num)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    if (String.valueOf(task.getResult().getValue()).equals("1")) {
                        Log.d("bgflag", "1");
                        seat[num].setBackground(ContextCompat.getDrawable(mContext, R.drawable.seat_x));
                    } else {
                        Log.d("bgflag", "0");
                        seat[num].setBackground(ContextCompat.getDrawable(mContext, R.drawable.seat_o));
                    }
                }
            }
        });
    }

    public class btnSeatListener implements View.OnClickListener {
        protected int num1;
        protected int num2;


        public btnSeatListener (int num1, int num2) {
            this.num1 = num1;
            this.num2 = num2;
        }

        public void onClick(View v){
            mDatabase.child("CAFE").child(cafe.concat(String.valueOf(num1))).child("seat").child(String.valueOf(num2)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        if(String.valueOf(task.getResult().getValue()).equals("1")){
                            result = "이미 예약된 좌석입니다.";
                            StyleableToast.makeText(AppointmentActivity.this, result, Toast.LENGTH_SHORT, R.style.mytoast).show();
                        }else{
                            //result = "예약이 가능한 좌석입니다.";
                            AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentActivity.this, R.style.MyAlertDialogTheme);
                            builder.setIcon(R.drawable.logo_white);
                            builder.setTitle("좌석 예약").setMessage("선택하신 좌석은 " + num2 + "번 입니다. \n예약하시겠습니까?");

                            builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id){
                                    if(point1 >= 100){
                                        point1 -= 100;
                                        ((UserActivity)UserActivity.mContext).readUsers(1);
                                        StyleableToast.makeText(AppointmentActivity.this, "결제 후 " + Integer.toString(point1) + " 포인트가 남았습니다.", Toast.LENGTH_SHORT, R.style.mytoast).show();
                                        mDatabase.child("CAFE").child(cafe.concat(String.valueOf(num1))).child("seat").child(String.valueOf(num2)).setValue(1);
                                        Intent complete = new Intent(AppointmentActivity.this, CompleteActivity.class);
                                        complete.putExtra("cafe_num", num1);
                                        complete.putExtra("seat_num", num2);
                                        startActivity(complete);
                                        finish();
                                    }else{
                                        StyleableToast.makeText(AppointmentActivity.this, "포인트가 부족해 좌석을 예약할 수 없습니다. 충전해주세요.", Toast.LENGTH_SHORT, R.style.mytoast).show();
                                        Intent fail = new Intent(AppointmentActivity.this, UserActivity.class);
                                        startActivity(fail);
                                    }
                                }
                            });

                            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id){
                                    StyleableToast.makeText(AppointmentActivity.this, "좌석 예약을 취소합니다.", Toast.LENGTH_SHORT, R.style.mytoast).show();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }

                    }
                }
            });
        }
    };
}





