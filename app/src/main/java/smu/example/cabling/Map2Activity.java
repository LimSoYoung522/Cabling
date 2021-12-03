package smu.example.cabling;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
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
                                            setMarker(marker1, 33.2712, 126.5354, R.drawable.ic_baseline_album_24,0);
                                            marker1.setOnClickListener(new Overlay.OnClickListener() {
                                                @Override
                                                public boolean onClick(@NonNull Overlay overlay) {

                                                    Toast.makeText(getApplication(), "마커 1 클릭", Toast.LENGTH_SHORT).show();
                                                    return false;
                                                }
                                            });

                                        }
                                    }


        );

        btnMark2.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            setMarker(marker2, 33.49957, 126.531076, R.drawable.ic_baseline_location_on_24,10);
                                            marker2.setOnClickListener(new Overlay.OnClickListener() {
                                                @Override
                                                public boolean onClick(@NonNull Overlay overlay) {

                                                    Toast.makeText(getApplication(), "마커 2 클릭", Toast.LENGTH_SHORT).show();
                                                    return false;
                                                }
                                            });

                                        }
                                    }


        );

        btnMark3.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            setMarker(marker3, 33.49957, 126.531128, R.drawable.ic_baseline_location_on_24,10);
                                            marker3.setOnClickListener(new Overlay.OnClickListener() {
                                                @Override
                                                public boolean onClick(@NonNull Overlay overlay) {

                                                    Toast.makeText(getApplication(), "마커 3 클릭", Toast.LENGTH_SHORT).show();
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
                new LatLng(37.65, 127.06),    //위치 지정
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
