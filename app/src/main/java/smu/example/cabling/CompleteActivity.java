package smu.example.cabling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.text.DateFormat.getDateTimeInstance;

public class CompleteActivity extends AppCompatActivity {
    TextView timeout;
    Button extend, sit;
    int seatnum, cafenum;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Intent intent = getIntent();
        seatnum = intent.getIntExtra("seat_num", 0);
        cafenum = intent.getIntExtra("cafe_num", 0);

        timeout = (TextView)findViewById(R.id.timeout);
        extend = (Button)findViewById(R.id.btnextend);
        sit = (Button)findViewById(R.id.btnsit);

        sit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteActivity.this, Map2Activity.class);
                Toast.makeText(getApplicationContext(), "메인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        //timeout.setText(getDateTimeInstance().format(new Date()));
        showtimemethod();
        }

    public void showtimemethod(){
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        //Date target = Calendar.getInstance().getTime();
        //long ltarget = Long.parseLong(dateFormat.format(target)) + 30;


        final Handler handler = new Handler(){
            int time = 10;
            @Override
            public void handleMessage(@NonNull Message msg) {
                //Date now = Calendar.getInstance().getTime();
                //timeout.setText(Long.toString(ltarget - Long.parseLong(dateFormat.format(now))) + "초 안에 자리에 착석해주세요.");
                if(time >= 1){
                    timeout.setText(Integer.toString(time) + "초 안에 자리에 착석해주세요.");
                    time--;

                    extend.setText("시간 연장하기");
                    extend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            time += 10;
                        }
                    });
                    sit.setText("착석 시, 버튼을 눌러주세요.");
                }else{
                    timeout.setText("지정된 시간 내에 착석하지 않아 예약이 취소되었습니다.\n 다시 예약해주세요.");
                    extend.setText("예약 화면으로 돌아가기");
                    extend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CompleteActivity.this, AppointmentActivity.class);
                            intent.putExtra("cafe_num", cafenum);

                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("CAFE").child("cafe1").child("seat").child(String.valueOf(seatnum)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    } else {
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                        String cafe = "cafe";
                                        mDatabase.child("CAFE").child(cafe.concat(String.valueOf(cafenum))).child("seat").child(String.valueOf(seatnum)).setValue(0);
                                    }
                                }
                            });

                            startActivity(intent);
                        }
                    });
                    sit.setText("메인 화면으로 돌아가기");
                }

            }
        };

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                        handler.sendEmptyMessage(1);
                    }
                }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
}
