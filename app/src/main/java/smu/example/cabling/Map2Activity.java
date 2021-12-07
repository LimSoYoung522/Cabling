package smu.example.cabling;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.Map;


public class Map2Activity extends AppCompatActivity implements OnMapReadyCallback  {
    private MapView mapView;
    private static NaverMap naverMap;
    Button btnMark1;
    Button btnMark2;
    Button btnMark3;

    private DatabaseReference mDatabase;
    Context mContext;
    String cafe = "cafe";
    String distance = "";
    String signature = "";

    private Marker marker1 = new Marker();
    private Marker marker2 = new Marker();
    private Marker marker3 = new Marker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        mContext = getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, MODE_PRIVATE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION }, MODE_PRIVATE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET }, MODE_PRIVATE);

        btnMark1 = (Button)findViewById(R.id.btnmark1);
        btnMark2 = (Button)findViewById(R.id.btnmark2);
        btnMark3 = (Button)findViewById(R.id.btnmark3);

        btnMark1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setMarker(marker1, 37.54554870789251, 126.966795744657, R.drawable.marker,0);
                marker1.setOnClickListener(new MarkerClickListener(1));
            }
        } );

        btnMark2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMarker(marker1, 37.54505953924471, 126.96675736948202, R.drawable.marker,0);
                marker1.setOnClickListener(new MarkerClickListener(2));
            }
        } );

        btnMark3.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                setMarker(marker1, 37.545198290177346, 126.96604613699941, R.drawable.marker,0);
                marker1.setOnClickListener(new MarkerClickListener(3));
            }
        } );


        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }

    public class getDSThread implements Runnable{
        int num;

        getDSThread(int n){ num = n; }
        @Override
        public void run() {
            mDatabase.child("CAFE").child(cafe.concat(String.valueOf(num))).child("distance").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) { distance = String.valueOf(dataSnapshot.getValue()); }
                @Override
                public void onCancelled(DatabaseError databaseError) {  }
            });

            mDatabase.child("CAFE").child(cafe.concat(String.valueOf(num))).child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) { signature = String.valueOf(dataSnapshot.getValue()); }
                @Override
                public void onCancelled(DatabaseError databaseError) {  }
            });
        }
    }

    public class setDSThread implements Runnable{
        @Override
        public void run() {
            try{
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public class MarkerClickListener implements Overlay.OnClickListener{
        protected int num;
        MarkerClickListener(int n){ num = n; }

        public boolean onClick(@NonNull Overlay overlay) {
            Thread t = new Thread(new getDSThread(num));
            t.start();
            try{
                t.join();

                AlertDialog.Builder builder = new AlertDialog.Builder(Map2Activity.this);
                builder.setTitle("CAFE" + num +  " 확인 창");
                builder.setMessage("선택하신 CAFE" + num + "로 예약 진행하시겠습니까 ?\n현재 위치로부터 " + distance + " 떨어져있습니다.\n대표 메뉴는 " + signature + " 입니다.");

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Map2Activity.this, AppointmentActivity.class);
                        intent.putExtra("cafe_num", num);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("아니오", null);
                builder.create().show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        //배경 지도 선택
        // naverMap.setMapType(NaverMap.MapType.Satellite);

        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(37.545384781090256, 126.96696471339739),    //위치 지정
                15                                        //줌 레벨
        );
        naverMap.setCameraPosition(cameraPosition);

    }

    private void setMarker(Marker marker, double lat, double lng, int resourceID, int zIndex) {
        //원근감 표시
        marker.setIconPerspectiveEnabled(true);
        // 아이콘 지정
        marker.setIcon(OverlayImage.fromResource(resourceID));
        //마커의 투명도
        marker.setAlpha(0.8f);
        //마커 위치
        marker.setPosition(new LatLng(lat, lng));
        //마커 우선순위
        marker.setZIndex(zIndex);
        //마커 표시
        marker.setMap(naverMap);
    }

    public void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }



}
