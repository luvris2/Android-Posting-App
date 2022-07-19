package com.luvris2.postingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.luvris2.postingapp.R;
import com.luvris2.postingapp.model.Posting;

public class EditActivity extends AppCompatActivity {

    EditText editTitle, editContent;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // 액션바 타이틀 설정
        getSupportActionBar().setTitle("포스팅 수정");
        // 액션바 back 버튼 설정
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);

        Posting Posting = (Posting) getIntent().getSerializableExtra("Posting");

        editTitle.setText(Posting.title);
        editContent.setText(Posting.content);

        btnSave.setOnClickListener(view -> {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();

            // 데이터를 전달하기 위한 intent
            Intent intent = new Intent();
            int index = getIntent().getIntExtra("index", 0);
            intent.putExtra("index", index);
            intent.putExtra("postingTitle", title);
            intent.putExtra("postingContent", content);
            setResult(2, intent);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // back 버튼 설정법
        // 1. finish()
        // 2. 기계의 back 버튼 눌렀을때의 콜백 메소드 onBackPressed();
        finish();
        return true;
    }

    // XML로 만든 메뉴를 화면에 나타나게 해주는 메소드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    // XML로 만든 메뉴를 누르면 다른 액티비티로 이동하게 해주는 메소드
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add) {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();

            // 데이터를 전달하기 위한 intent
            Intent intent = new Intent();
            int index = getIntent().getIntExtra("index", 0);
            intent.putExtra("index", index);
            intent.putExtra("postingTitle", title);
            intent.putExtra("postingContent", content);
            setResult(2, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}