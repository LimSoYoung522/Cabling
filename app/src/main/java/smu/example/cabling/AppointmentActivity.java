package smu.example.cabling;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppointmentActivity extends AppCompatActivity {
    private static final String TAG = "map";
    private DatabaseReference mDatabase;
    Button seat[];
    String result = "";
    int i;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        seat = new Button[10];
        int[] seatID = {
                R.id.seat0, R.id.seat1, R.id.seat2,
                R.id.seat3, R.id.seat4, R.id.seat5,
                R.id.seat6, R.id.seat7, R.id.seat8,
                R.id.seat9
        };

        for (i = 0; i < 10; i++) {
            this.seat[i] = (Button) findViewById(seatID[i]);
            this.seat[i].setOnClickListener(new btnSeatListener(1, i));
        }
    }

    /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/
    public class btnSeatListener implements View.OnClickListener {
        protected int num1;
        protected int num2;
        String cafe = "cafe";

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
                            result = "이미 선택된 좌석입니다.";
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }else{
                            //result = "예약이 가능한 좌석입니다.";
                            AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentActivity.this);

                            builder.setTitle("좌석 예약").setMessage("선택하신 좌석은 @입니다. 예약하시겠습니까?");

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id){
                                    Toast.makeText(getApplicationContext(), "결제 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                                    // 결제 화면 액티비티 및 예약 완료 창 만들기
                                    mDatabase.child("CAFE").child(cafe.concat(String.valueOf(num1))).child("seat").child(String.valueOf(num2)).setValue(1);
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id){
                                    Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
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





