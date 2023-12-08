package com.example.caretravel;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class map extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        Log.d("phj","지도 들어옴");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            Geocoder geocoder = new Geocoder(this);

            //방이름이랑 데이터베이스에서 이름에 맞는 몇일차 정보 가져와서 이름 정의하기
            //표에 저장되어있는 장소, 위도, 경도, 일차 가져오기
            //String str = searchName.getText().toString();

                    List<Address> addressList = null;
                    try {
                        // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                        addressList = geocoder.getFromLocationName(
                                str, // 주소
                                10); // 최대 검색 결과 개수
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("phj","경로 주소 리스트"+addressList.get(0).toString());

                    //콤마를 기준으로 split
                    String []splitStr = addressList.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                    Log.d("phj","주소"+address);

                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                    Log.d("phj","위도"+latitude);
                    Log.d("phj","경로"+longitude);


//                    String latitude = String.valueOf(37.4944064);
//                    String longitude = String.valueOf((126.9599747));
//                    String address = "aa";

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
