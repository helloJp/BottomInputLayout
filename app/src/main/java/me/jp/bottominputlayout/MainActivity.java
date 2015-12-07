package me.jp.bottominputlayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.jp.bottominputlayout.view.BottomInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    BottomInputLayout inputLayout;
    private ViewGroup layoutComment;
    private List<User> mUsers = new ArrayList<>();
    private int BASE_ID = 0X87;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initEvent();

    }

    private void initData() {
        for (int i = 0; i < 6; i++) {
            User user = new User(i, "bill " + i, " there are " + i + " bills");
            mUsers.add(user);
        }
    }

    private void initEvent() {
        inputLayout.setOnClickCallback(new MyCallback());
    }

    private class MyCallback implements BottomInputLayout.OnClickCallback {

        @Override
        public void onShowBigImg(View view) {
            showToast("show big img");
        }

        @Override
        public void onAddPic(View view) {
            showToast("add img");

        }

        @Override
        public void onAction(View view) {
            showToast("action ");

        }

        @Override
        public void onMaskClick(View view) {
            inputLayout.getInputView().setHint("reply article content");
            inputLayout.getInputView().setTag(0);
        }
    }

    private void initView() {
        inputLayout = (BottomInputLayout) findViewById(R.id.input_layout);
        layoutComment = (ViewGroup) findViewById(R.id.layout_comment);

        for (int i = 0; i < mUsers.size(); i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setText(mUsers.get(i).name + ":  " + mUsers.get(i).comment);
            textView.setPadding(dip2px(10), dip2px(20), dip2px(10), dip2px(20));
            textView.setBackgroundColor(i % 2 == 0 ? Color.GRAY : Color.LTGRAY);
            textView.setId(BASE_ID + i);
            textView.setOnClickListener(this);
            layoutComment.addView(textView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        for (int i = 0; i < layoutComment.getChildCount(); i++) {
            if (id == layoutComment.getChildAt(i).getId()) {
                inputLayout.showSoftInput();
                //set reply object
                inputLayout.getInputView().setHint("reply: " + mUsers.get(i).name);
                inputLayout.getInputView().setTag(mUsers.get(i).id);
            }
        }
    }

    private int dip2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    public void debug(String msg) {
        Log.i("debug", msg);
    }

    public void showToast(String msg) {
        if (null != msg)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private class User {
        public int id;
        public String name;
        public String comment;

        public User(int id, String name, String comment) {
            this.id = id;
            this.name = name;
            this.comment = comment;
        }
    }

}
