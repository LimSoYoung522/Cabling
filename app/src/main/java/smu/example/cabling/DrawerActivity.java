package smu.example.cabling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;

public class DrawerActivity extends AppCompatActivity implements OnMapReadyCallback  {

    private static final String TAG = "Drawer_Activity";

    private Context mContext = DrawerActivity.this;
    private NavigationView nav;

    private MapView mapView;
    private static NaverMap naverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        //((UserActivity)UserActivity.mContext).readUsers(0);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, MODE_PRIVATE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION }, MODE_PRIVATE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET }, MODE_PRIVATE);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        init();
        NavigationViewHelper.enableNavigation(mContext, nav);
    }
    private void init(){
        nav = findViewById(R.id.nav);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        Marker marker1 = new Marker();
        marker1.setIcon(OverlayImage.fromResource(R.drawable.marker));
        marker1.setPosition(new LatLng(37.54554870789251, 126.966795744657));
        marker1.setMap(naverMap);
        marker1.setOnClickListener(new mapClickListener(1));

        Marker marker2 = new Marker();
        marker2.setIcon(OverlayImage.fromResource(R.drawable.marker));
        marker2.setPosition(new LatLng(37.54505953924471, 126.96675736948202));
        marker2.setMap(naverMap);
        marker2.setOnClickListener(new mapClickListener(2));

        Marker marker3 = new Marker();
        marker3.setIcon(OverlayImage.fromResource(R.drawable.marker));
        marker3.setPosition(new LatLng(37.545198290177346, 126.96604613699941));
        marker3.setMap(naverMap);
        marker3.setOnClickListener(new mapClickListener(3));

        //?????? ??????
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(37.545384781090256, 126.96696471339739),    //?????? ??????
                17                                      //??? ??????
        );
        naverMap.setCameraPosition(cameraPosition);

    }

    public class mapClickListener implements Overlay.OnClickListener {
        protected int num1;

        public mapClickListener(int num1) {
            this.num1 = num1;
        }

        public boolean onClick(@NonNull Overlay overlay) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this, R.style.MyAlertDialogTheme);
            builder.setIcon(R.drawable.logo_white);
            builder.setTitle("CAFE" + num1 + " ?????? ???");
            if(num1 == 1){
                builder.setMessage("???????????? CAFE " + num1 + "??? ?????? ???????????????????????? ?\n?????? ??????????????? 1km ?????????????????????.\n?????? ????????? ?????????????????????.");
            }else if(num1 == 2){
                builder.setMessage("???????????? CAFE " + num1 + "??? ?????? ???????????????????????? ?\n?????? ??????????????? 3km ?????????????????????.\n?????? ????????? ?????????????????????.");
            }else if(num1 == 3){
                builder.setMessage("???????????? CAFE " + num1 + "??? ?????? ???????????????????????? ?\n?????? ??????????????? 5km ?????????????????????.\n?????? ????????? ????????????????????????.");
            }
            builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DrawerActivity.this, AppointmentActivity.class);
                    intent.putExtra("cafe_num", num1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_splash_fadein, R.anim.anim_splash_fadeout);
                    //finish();
                }
            });
            builder.setNegativeButton("?????????", null);
            builder.create().show();
            return false;
        }
    }
    private void setMarker(Marker marker, double lat, double lng, int resourceID, int zIndex) {
        //????????? ??????
        marker.setIconPerspectiveEnabled(true);
        // ????????? ??????
        marker.setIcon(OverlayImage.fromResource(resourceID));
        //????????? ?????????
        marker.setAlpha(0.8f);
        //?????? ??????
        marker.setPosition(new LatLng(lat, lng));
        //?????? ????????????
        marker.setZIndex(zIndex);
        //?????? ??????
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