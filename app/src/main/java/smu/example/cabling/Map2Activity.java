package smu.example.cabling;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;


public class Map2Activity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private static NaverMap naverMap;
    Button btnMark1;
    Button btnMark2;
    Button btnMark3;


    private Marker marker1 = new Marker();
    private Marker marker2 = new Marker();
    private Marker marker3 = new Marker();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

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
                                            marker1.setOnClickListener(new Overlay.OnClickListener() {
                                                @Override
                                                public boolean onClick(@NonNull Overlay overlay) {

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Map2Activity.this);
                                                    builder.setTitle("CAFE1 확인 창");
                                                    builder.setMessage("선택하신 CAFE1로 예약 진행하시겠습니까 ?\n현재 위치로부터 1km 떨어져있습니다.\n대표 메뉴는 초코라떼입니다.");
                                                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Map2Activity.this, AppointmentActivity.class);
                                                            intent.putExtra("cafe_num", 1);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    builder.setNegativeButton("아니오", null);
                                                    builder.create().show();
                                                    return false;
                                                }
                                            });

                                        }
                                    }


        );

        btnMark2.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v) {
                                            setMarker(marker1, 37.54505953924471, 126.96675736948202, R.drawable.marker,0);
                                            marker1.setOnClickListener(new Overlay.OnClickListener() {
                                                @Override
                                                public boolean onClick(@NonNull Overlay overlay) {

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Map2Activity.this);
                                                    builder.setTitle("CAFE2 확인 창");
                                                    builder.setMessage("선택하신 CAFE2로 예약 진행하시겠습니까 ?\n현재 위치로부터 3km 떨어져있습니다.\n대표 메뉴는 말차라떼입니다.");
                                                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Map2Activity.this, AppointmentActivity.class);
                                                            intent.putExtra("cafe_num", 2);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    builder.setNegativeButton("아니오", null);
                                                    builder.create().show();
                                                    return false;
                                                }
                                            });

                                        }
                                    }


        );

        btnMark3.setOnClickListener(new View.OnClickListener()  {
                                        @Override
                                        public void onClick(View v) {
                                            setMarker(marker1, 37.545198290177346, 126.96604613699941, R.drawable.marker,0);
                                            marker1.setOnClickListener(new Overlay.OnClickListener() {
                                                @Override
                                                public boolean onClick(@NonNull Overlay overlay) {

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Map2Activity.this);
                                                    builder.setTitle("CAFE3 확인 창");
                                                    builder.setMessage("선택하신 CAFE3로 예약 진행하시겠습니까 ?\n현재 위치로부터 5km 떨어져있습니다.\n대표 메뉴는 아메리카노입니다.");
                                                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Map2Activity.this, AppointmentActivity.class);
                                                            intent.putExtra("cafe_num", 3);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    builder.setNegativeButton("아니오", null);
                                                    builder.create().show();
                                                    return false;
                                                }
                                            });

                                        }
                                    }



        );

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);




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
