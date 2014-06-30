package com.mogujie.findmyphone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Set;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class SetAliasAct extends InstrumentedActivity {

    private static String TAG = "MIT";
    private ProgressBar pb;
    private EditText mEtAlias;
    private Button setAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        String alias = sharedPreferences.getString("alias", "");

        if (alias.equals("")) {
            pb = (ProgressBar) findViewById(R.id.progress);
            mEtAlias = (EditText) findViewById(R.id.et_addAlias);
            setAlias = (Button) findViewById(R.id.btn_add_alias);
            setAlias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAlias();
                }
            });
        } else {
            Intent intent = new Intent(this, HomeAct.class);
            startActivity(intent);
            finish();
        }
    }

    private void setAlias() {
        pb.setVisibility(View.VISIBLE);
        pb.setIndeterminate(false);
        setAlias.setVisibility(View.INVISIBLE);
        if (mEtAlias.getText().length() != 0) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, mEtAlias.getText().toString()));
        } else {
            mEtAlias.setError("输入的内容不正确！");
        }
    }

    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAlias(getApplicationContext(), (String) msg.obj, mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            pb.setVisibility(View.GONE);
            setAlias.setVisibility(View.VISIBLE);
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    Toast.makeText(SetAliasAct.this, "设置成功！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("alias", mEtAlias.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    Toast.makeText(SetAliasAct.this, "设置失败，状态码：" + code, Toast.LENGTH_LONG).show();
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

        }
    };

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
