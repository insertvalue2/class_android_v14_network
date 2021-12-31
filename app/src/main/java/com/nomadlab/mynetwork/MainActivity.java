package com.nomadlab.mynetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nomadlab.mynetwork.models.response.Todo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //    String urlString = "https://jsonplaceholder.typicode.com/posts";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 2.
        // JSONObject 만드는 방법
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("이름", "홍길동");
            jsonObj.put("나이", 23);
            jsonObj.put("직업", "CEO");
            jsonObj.put("취미", "노래");
            jsonObj.put("결혼여부", false);

            // JSONObject 에서 값 꺼내 보기
            Log.d("TAG", jsonObj.getString("이름"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //        Log.d("TAG", jsonObj.toString());
        // JSONArray
        JSONArray jsonObjectArray = new JSONArray();
        jsonObjectArray.put(jsonObj);
        jsonObjectArray.put(jsonObj);
        jsonObjectArray.put(jsonObj);

//        Log.d("TAG", jsonObjectArray.toString());

        // jsonObjectArray 에서 값 꺼내기
        try {
            JSONObject arrJson0 = jsonObjectArray.getJSONObject(0);
            Log.d("TAG", "이름 : " + arrJson0.get("이름"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // { arr : [ {} , {}]}
        JSONObject jsonObj2 = new JSONObject();
        try {
            jsonObj2.put("arr", jsonObjectArray);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Log.d("TAG", jsonObj2.toString());

        // json parsing
        JSONObject obj;
        try {
            obj = new JSONObject(jsonObj2.toString());

            JSONArray array = obj.getJSONArray("arr");
            JSONObject dataObj = array.getJSONObject(0);

            String friendName = dataObj.getString("이름");
            String friendAge = dataObj.getString("나이");

            Log.d("TAG", "friendName:" + friendName);
            Log.d("TAG", "friendAge:" + friendAge);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpGetTest();

    }

    private void httpGetTest() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();

        new Thread(() -> {
            try {
                String urlString = "https://jsonplaceholder.typicode.com/todos";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                //
                //connection.setRequestMethod("POST");
                // application/json, request body 를  JSON 으로 던져줌
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                //
//                connection.setRequestProperty("Accept", "application/json");
                // Output Stream POST 데이터로 전동


                // String buffer = "";
                Log.d("TAG", "status : " + connection.getResponseCode() + "");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == 201) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream(),
                                    "UTF-8"));
                    // 1.
                    // 한 라인(한줄)
                    // buffer = reader.readLine();

                    // 2.
//                    String line = null;
//                    StringBuffer sb = new StringBuffer();
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line + "\n");
//                    }
//                    Log.d("TAG", sb.toString());

                    //3 . 배열 타입으로 변환 방법
                    Todo[] todos = new Gson().fromJson(reader, Todo[].class);

                    // ArrayList 타입으로 변환
                    Type userListType = new TypeToken<ArrayList<Todo>>() {
                    }.getType();

                    ArrayList<Todo> todoArrayList = new Gson().fromJson(reader, userListType);

                    for (Todo todo: todoArrayList) {

                        Log.d("TAG", todo.id + " : id");
                        Log.d("TAG", todo.userId + " : name");
                        Log.d("TAG", todo.completed + " : age");
                    }

                } else {
                    Log.d("TAG", "http fail ");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void httpPostTest() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();

        new Thread(() -> {
            try {
                String urlString = "https://jsonplaceholder.typicode.com/posts";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                //
                //connection.setRequestMethod("POST");
                // application/json, request body 를  JSON 으로 던져줌
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                //
//                connection.setRequestProperty("Accept", "application/json");
                // Output Stream POST 데이터로 전동


                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("이름", "홍길동");
                    jsonObj.put("나이", 23);
                    jsonObj.put("직업", "CEO");
                    jsonObj.put("취미", "노래");
                    jsonObj.put("결혼여부", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                connection.setDoInput(true);
                String jsonInputString = "{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }";

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonObj.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }


                // String buffer = "";
                Log.d("TAG", "status : " + connection.getResponseCode() + "");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == 201) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream(),
                                    "UTF-8"));
                    // 한 라인(한줄)
                    // buffer = reader.readLine();
                    String line = null;
                    StringBuffer sb = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    Log.d("TAG", sb.toString());
                } else {
                    Log.d("TAG", "http fail ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}