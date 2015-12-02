package me.jp.bottominputlayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import me.jp.bottominputlayout.view.BottomInputLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomInputLayout inputLayout = (BottomInputLayout) findViewById(R.id.input_layout);
        inputLayout.setOnActionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("action");
            }
        });
      }

    public void debug(String msg) {
        Log.i("debug", msg);
    }

    public void showToast(String msg) {
        if (null != msg)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
