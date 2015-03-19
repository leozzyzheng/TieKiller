package com.leozzyzheng.tiekiller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.leozzyzheng.tiekiller.account.AccountManger;
import com.leozzyzheng.tiekiller.http.HttpEngineProvider;
import com.leozzyzheng.tiekiller.http.request.LoginRequest;

public class MyActivity extends Activity {

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
        setContentView(R.layout.main);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mResult = (TextView) findViewById(R.id.result);
        mBtn = (TextView) findViewById(R.id.login);

        HttpEngineProvider.getInstance().createVollyEngine(this);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                LoginRequest request = new LoginRequest(username, password, new LoginRequest.OnLoginListenr() {
                    @Override
                    public void onLoginSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResult.setText(AccountManger.getInstance().getBdusstoken());
                            }
                        });
                    }

                    @Override
                    public void onLoginFailed(String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResult.setText("登陆失败");
                            }
                        });
                    }
                });

                request.send();
            }
        });
    }


}
