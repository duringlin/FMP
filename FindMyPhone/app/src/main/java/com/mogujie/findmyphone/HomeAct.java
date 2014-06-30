package com.mogujie.findmyphone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mogujie.findmyphone.R;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

public class HomeAct extends InstrumentedActivity {
    SharedPreferences sharedPreferences;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textView = (TextView) findViewById(R.id.textview);
        Button setting = (Button) findViewById(R.id.to_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAct.this,SetAliasAct.class);
                startActivityForResult(intent,1001);
            }
        });
        sharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);
        String alias =sharedPreferences.getString("alias","");

        if(alias.equals("")){
            textView.setText("请为该设备设置别名！");
        }else{
            textView.setText("设备别名："+alias+"\n 程序正在运行");
        }
    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == RESULT_OK){
            String alias = data.getStringExtra("alias");
            sharedPreferences.edit().putString("alias", alias).apply();
            textView.setText("设备别名："+alias+"\n 程序正在运行");
        }
        else if(requestCode == 1001 && resultCode == RESULT_CANCELED){
            String alias =sharedPreferences.getString("alias","");

            if(alias.equals("")){
                textView.setText("请为该设备设置别名！");
            }else{
                textView.setText("设备别名："+alias+"\n 程序正在运行");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SetAliasAct.class);
            startActivityForResult(intent,1001);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
