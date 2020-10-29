package com.android.summer.logic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.launcher3.Launcher;
import com.android.launcher3.R;
import com.summer.logic.HttpUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogicActivity extends Activity implements View.OnClickListener {

    View cancleShutDownView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_logic);
        cancleShutDownView = findViewById(R.id.cancleshowdown);
        cancleShutDownView.setOnClickListener(this);
        startActivity(new Intent(this, Launcher.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //取消关机
            case R.id.cancleshowdown:
                Toast.makeText(this,"解除关闭电脑",Toast.LENGTH_LONG).show();
                HttpUtil.getInstance().cancleShutDown().enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
                break;
        }
    }
}
