package com.luvris2.postingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.luvris2.postingapp.adapter.Adapter;
import com.luvris2.postingapp.model.Posting;
import com.luvris2.postingapp.ui.AddActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Posting> postingList;
    FloatingActionButton fab;
    Posting posting;
    int newId;

    final String URL = "https://block1-image-test.s3.ap-northeast-2.amazonaws.com/posting.json";

    public ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 1) {
                        String title = result.getData().getStringExtra("postingTitle");
                        String content = result.getData().getStringExtra("postingContent");

                        Posting posting = new Posting(newId, 7, title, content);
                        postingList.add(posting);
                    }
                    else if (result.getResultCode() == 2) {
                        String title = result.getData().getStringExtra("postingTitle");
                        String content = result.getData().getStringExtra("postingContent");
                        int index = result.getData().getIntExtra("index", 0);

                        posting = postingList.get(index);
                        posting.title = title;
                        posting.content = content;
                    }
                    adapter.notifyDataSetChanged();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션바 호출
        getSupportActionBar().setTitle("포스팅 리스트");

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        getData();

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            newId = postingList.get(postingList.size() - 1).id + 1;

            startActivityResult.launch(intent);
        });
    }

    // XML로 만든 메뉴를 화면에 나타나게 해주는 메소드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // XML로 만든 메뉴를 누르면 다른 액티비티로 이동하게 해주는 메소드
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            newId = postingList.get(postingList.size() - 1).id + 1;
            startActivityResult.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        // 네트워크 통신을 위한 JsonObjectRequest 객체 생성
        // 생성자 : http Method, API URL, 전달 할 데이터, 실행 코드(Listener)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    // API 호출 결과 실행
                    try {
                        postingList = new ArrayList<>();

                        JSONArray jarr = response.getJSONArray("data");

                        for (int i=0; i<jarr.length(); i++){
                            JSONObject c = jarr.optJSONObject(i);
                            int id = c.optInt("id");
                            int userId = c.optInt("userId");
                            String title = c.optString("title");
                            String content = c.optString("body");
                            Posting posting = new Posting(id, userId, title, content);
                            postingList.add(posting);
                        }
                        adapter = new Adapter(MainActivity.this, postingList);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Log.i("onErrorResponse", ""+error);
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add((jsonObjectRequest));
    }
}