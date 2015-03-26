package com.leozzyzheng.tiekiller.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.account.AccountManger;
import com.leozzyzheng.tiekiller.http.HttpEngineProvider;
import com.leozzyzheng.tiekiller.http.request.AutoLoginRequest;
import com.leozzyzheng.tiekiller.http.request.LoginRequest;
import com.leozzyzheng.tiekiller.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    private EditText mUsername;
    private EditText mPassword;
    private TextView mResult;
    private TextView mBtn;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mResult = (TextView) findViewById(R.id.result);
        mBtn = (TextView) findViewById(R.id.login);


        //从配置中读取数据
        AccountManger.getInstance().loadFromFile(this);
        //初始化Http请求引擎
        HttpEngineProvider.getInstance().createVollyEngine(this);

        //如果已经有配置，则用配置自动登录
        if (AccountManger.getInstance().getBdusstoken().length() != 0) {
            AutoLoginRequest request = new AutoLoginRequest(new AutoLoginRequest.OnLoginListenr() {
                @Override
                public void onLoginSuccess() {
                    AccountManger.getInstance().saveToFile(LoginActivity.this);
                    Toast.makeText(LoginActivity.this, "自动登陆成功", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.onLoginSuccess();
                }

                @Override
                public void onLoginFailed(final String message) {
                    mResult.setText(message);
                    enableLoginBtn();
                }
            });
            mBtn.setText("正在自动登录...");
            mBtn.setEnabled(false);
            request.send();
        } else {
            enableLoginBtn();
        }
    }

    /**
     * 切换为自动登录
     */
    private void enableLoginBtn() {
        mBtn.setText(getString(R.string.login_in_btn));
        mBtn.setEnabled(true);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                LoginRequest request = new LoginRequest(username, password, new LoginRequest.OnLoginListenr() {
                    @Override
                    public void onLoginSuccess() {
                        AccountManger.getInstance().saveToFile(LoginActivity.this);
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        LoginActivity.this.onLoginSuccess();
                    }

                    @Override
                    public void onLoginFailed(final String message) {
                        mResult.setText(message);

                    }
                });

                request.send();
            }
        });
    }

    private void onLoginSuccess() {
        startActivity(new Intent(this, ForumActivity.class));
        finish();
    }
}
