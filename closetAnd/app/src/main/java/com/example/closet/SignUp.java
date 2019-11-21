package com.example.closet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button cancel, ok;
    EditText nickName, id, pwd, age;
    RadioGroup radioGroup;
    RadioButton female, male;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setting();
    }

    public void setting() {
        cancel = (Button) findViewById(R.id.cancel_signUp);
        ok = (Button) findViewById(R.id.ok_singUp);
        radioGroup = (RadioGroup) findViewById(R.id.sexGroup);
        female = (RadioButton) findViewById(R.id.female);
        male = (RadioButton) findViewById(R.id.male);
        female.setOnClickListener(this);
        male.setOnClickListener(this);
        nickName = (EditText) findViewById(R.id.nickname_signUp);
        id = (EditText) findViewById(R.id.id_signUp);
        pwd = (EditText) findViewById(R.id.pwd_signUp);
        age = (EditText) findViewById(R.id.age_signUp);
        female.setChecked(true);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == cancel) {
            //intent로 돌려보내기
        }
        if (view == ok) {
            String nickName = this.nickName.getText().toString();
            String id = this.id.getText().toString();
            String pwd = this.pwd.getText().toString();
            String age = this.age.getText().toString();
            String sex = "0";
            if (male.isChecked()) {
                sex = "1";
                female.setChecked(false);
            } else if (female.isChecked()) {
                sex = "0";
                male.setChecked(false);
            }
            if(nickName.equals("") || id.equals("") || pwd.equals("") || age.equals("")) {
                //돌려보내기
                //각 내용 유효한 값인지 확인. 나이같은거
                Log.d("Log_dSingup","입력 안된 항목이 있습니다");
            }
            //네트워킹
            else {
                HashMap<String,String> arguments = new HashMap<>();
                arguments.put("id",id);
                arguments.put("pwd",pwd);
                arguments.put("nickname",nickName);
                arguments.put("age",age);
                arguments.put("sex",sex);

                try {
                    Networking networking = new Networking(new URL("http://52.78.194.160:3000/user/signup"),arguments);
                    networking.execute();
                    networking.get();
                    JSONObject result = networking.get();
                    Log.d("Log_dResult", String.valueOf(result));
                    //각자 그 이후 결과 알려주고 intent
                    if(result.get("status").equals(201)){
                    //class java.lang.Integer 로 오네
                        Log.d("Log_d 회원가입 성공","ㄴㄻㄹ");
                        Intent intent  = new Intent(this,MainActivity.class);
                        startActivity(intent);
                        //uid 정보 put해서 전달하고 저장.
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

        }
    }
}


