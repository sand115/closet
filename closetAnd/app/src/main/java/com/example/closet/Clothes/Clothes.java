package com.example.closet.Clothes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.Spinner;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import com.example.closet.Networking_Get;
import com.example.closet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Clothes extends AppCompatActivity implements OnItemSelectedListener {
    private Clothes_Adapter adapter;
    ArrayList<URL> photoUrls = new ArrayList<>();
    GridView gridView;

    //image to server
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    String selectedImagesPaths;
    boolean imagesSelected = false;
    private Uri uri;
    Bitmap bitmap;
    //image to server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);
        getClothings(null);
        loadGridView();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner4);
        spinner.setOnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(this);
        String[] items = getResources().getStringArray(R.array.clothes_array);
        String[] items1 = getResources().getStringArray(R.array.clothes_color_array);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(dataAdapter);
        spinner1.setAdapter(dataAdapter1);
    }

    private void getClothings(String category) {
        try {
            //URL url = new URL("http://52.78.194.160:3000/closet/show/personalCloset/1/null"); //uid 고치기
            URL url = new URL("http://52.78.194.160:3000/closet/show/personalCloset/1/" + category);
            Networking_Get networking = new Networking_Get(url);
            networking.execute();
            JSONObject result = networking.get();
            JSONArray clothingResults = (JSONArray) result.get("result");
            //Log.d("Log_d_jsonarray", String.valueOf(clothingResults));
            for (int i = 0; i < clothingResults.length(); i++) {
                JSONObject eachClothing = clothingResults.getJSONObject(i);
                String photoFile = eachClothing.getString("photo");
                //Log.d("Log_dPhotoFile",photoFile);
                photoUrls.add(new URL(photoFile));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadGridView() {
        gridView = (GridView) findViewById(R.id.clothes_grid);
        adapter = new Clothes_Adapter(this, R.layout.clothes_griditem, photoUrls);
        gridView.setAdapter(adapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                addPopup();
                break;
            case R.id.info:
                infoPopup();
                break;
            case R.id.mypick:
                break;
        }
    }

    private PopupWindow infoPopupWindow;
    private PopupWindow addPopupWindow;

    protected void addPopup() {
        View addPopupView = getLayoutInflater().inflate(R.layout.activity_clothes3, null);
        addPopupWindow = new PopupWindow(addPopupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
        addPopupWindow.setFocusable(true);
        // 외부 영역 선택시 PopUp 종료
        addPopupWindow.showAtLocation(addPopupView, Gravity.CENTER, 0, 0);

        Button close = (Button) addPopupView.findViewById(R.id.closeAdd);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPopupWindow.dismiss();
            }
        });

        Button camera = (Button) addPopupView.findViewById(R.id.callCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Camera", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        });

        Button album = (Button) addPopupView.findViewById(R.id.callAlbum);
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Album", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
    }

    protected void infoPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.activity_clothes2, null);
        infoPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
        infoPopupWindow.setFocusable(true);
        // 외부 영역 선택시 PopUp 종료
        infoPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        Button cancel = (Button) popupView.findViewById(R.id.Cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPopupWindow.dismiss();
            }
        });

        Button ok = (Button) popupView.findViewById(R.id.Ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        System.out.println(item);
        if(!(item.equals("Category"))) {
            // Showing selected spinner item
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            photoUrls.clear();
            getClothings(item);
            loadGridView();
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_CAMERA){

        }
        else if (requestCode == PICK_FROM_ALBUM) {
            String currentImagePath;
            //selectedImagesPaths = new ArrayList<>();
            if (data == null) {
                Log.d("Log_d data", "data is null");
            }
            else {
                uri = data.getData();
                currentImagePath = DocumentsContract.getDocumentId(uri);
                String [] realPath = currentImagePath.split(":");
                selectedImagesPaths = realPath[1];
                imagesSelected = true;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    Log.d("Log_d bitmap객체 : ",bitmap.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        AddClothes sendImage = new AddClothes(imagesSelected, selectedImagesPaths, bitmap);
        sendImage.connectServer();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
