package smu.example.cabling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.text.DateFormat.getDateTimeInstance;

public class CompleteActivity extends AppCompatActivity {
    TextView timeout;
    Button extend;
    int seatnum;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Intent intent = getIntent();
        seatnum = intent.getExtras().getInt("seatnum");
        timeout = (TextView)findViewById(R.id.timeout);
        extend = (Button)findViewById(R.id.btnextend);

        //timeout.setText(getDateTimeInstance().format(new Date()));
        showtimemethod();
        }

    public void showtimemethod(){
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        //Date target = Calendar.getInstance().getTime();
        //long ltarget = Long.parseLong(dateFormat.format(target)) + 30;


        final Handler handler = new Handler(){
            int time = 30;
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
                }else{
                    timeout.setText("지정된 시간 내에 착석하지 않아 예약이 취소되었습니다.\n 다시 예약해주세요.");
                    extend.setText("예약 화면으로 돌아가기");
                    extend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CompleteActivity.this, AppointmentActivity1.class);
                            intent.putExtra("cancel", seatnum);
                            startActivity(intent);
                        }
                    });
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
