package com.example.caretravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class path extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    Button searchButton;
    EditText searchbar;
    String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        Log.d("phj","지도 방 들어옴");
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("currentRoomName", null);
        Log.d("phj", "지도 방이름 " + roomName);

        searchButton = findViewById(R.id.search_button);
        searchbar = findViewById(R.id.search_bar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(path.this);

        //저장하는 표 만들어서 저장하기, 일수 추가 등등
    }



        @Override
        public void onMapReady(GoogleMap googleMap)
        {
            map = googleMap;
            Geocoder geocoder = new Geocoder(this);

//            map.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
//                @Override
//                public void onMapClick(LatLng point) {
//                    MarkerOptions mOptions = new MarkerOptions();
//                    // 마커 타이틀
//                    mOptions.title("마커 좌표");
//                    Double latitude = point.latitude; // 위도
//                    Double longitude = point.longitude; // 경도
//                    // 마커의 스니펫(간단한 텍스트) 설정
//                    mOptions.snippet(latitude.toString() + ", " + longitude.toString());
//                    // LatLng: 위도 경도 쌍을 나타냄
//                    mOptions.position(new LatLng(latitude, longitude));
//                    // 마커(핀) 추가
//                    googleMap.addMarker(mOptions);
//                }
//            });

            searchButton.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    String str=searchbar.getText().toString();
                    Log.d("phj","버튼 클릭"+str);

                    //위까지 오류없음
//                    List<Address> addressList = null;
//                    try {
//                        // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
//                        addressList = geocoder.getFromLocationName(
//                                str, // 주소
//                                10); // 최대 검색 결과 개수
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("phj","경로 주소 리스트"+addressList.get(0).toString());
//
//                    //콤마를 기준으로 split
//                    String []splitStr = addressList.get(0).toString().split(",");
//                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
//                    Log.d("phj","주소"+address);
//
//                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
//                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
//                    Log.d("phj","위도"+latitude);
//                    Log.d("phj","경로"+longitude);
                    //아래부터 오류없음

                    String latitude = String.valueOf(37.4944064);
                    String longitude = String.valueOf((126.9599747));
                    String address = "aa";

                    // 좌표(위도, 경도) 생성
                    LatLng point1 = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    // 마커 생성
                    MarkerOptions mOptions1 = new MarkerOptions();
                    mOptions1.title("search result");
                    mOptions1.snippet(address);
                    mOptions1.position(point1);
                    // 마커 추가
                    map.addMarker(mOptions1);
                    // 해당 좌표로 화면 줌
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(point1,15));
                }
            });

//            LatLng path1 = new LatLng(37.4944064, 126.9599747);
//            map.addMarker(new MarkerOptions().position(path1).title("숭실대학교 정보과학관"));
//
//            //두번째 장소 위도 경도
//            path1 = new LatLng(37.4963538, 126.9572222);
//            map.addMarker(new MarkerOptions().position(path1).title("숭실대학교"));
//
//            //세번째 장소 위도 경도
//            path1 = new LatLng(37.495861, 126.953991);
//            map.addMarker(new MarkerOptions().position(path1).title("숭실대입구 역"));
//            //여기서 장소 입력 할 수 있는 칸이 동적 추가 되면 될듯한데.., 그리고 날짜 별로 path2 ,3 이렇게 이름 넣기
//
//            map.moveCamera(CameraUpdateFactory.newLatLng(path1));
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(path1,20));//지도를 14배율로 확대해서 보여줌


        }

}