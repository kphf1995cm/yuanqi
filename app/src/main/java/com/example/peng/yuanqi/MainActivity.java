package com.example.peng.yuanqi;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static URL serverUrl=null;//服务器url
    public boolean isWebConnected = false;//网络状态
    public int userCurState = 0;//0，未登录，1，已登录
    public boolean isUserHeaderSetted = false;//用户头像是否已经被设置，头像存储在本地文件系统上
    private int loginResult;//登录成功或失败的结果
    EditText userAccount = null;
    EditText userPassword = null;
    ImageView userHeader=null;
    Button logIn = null;
    Button register = null;
    public static String curContactPersonAccount=null;
    public static ConnectWebClass connectWebClass=new ConnectWebClass();
    public static User loginUser=new User();

    //public Drawable getLocalDrawable()｛
    //    ImageView imageView=(ImageView)findViewById(R.id.user_header);
    //Drawable mydrawable=(Drawable)imageView.getDrawable();
    //    return mydrawable;
    //｝;//从本地文件系统中取出头像
    //检测用户账号和密码是否存在及匹配,返回0，成功，1，账号不存在，2，密码错误

    public boolean isNetworkConnected(Context context) {
        //if (context != null) {
         //   ConnectivityManager connectivityManager = (ConnectivityManager) context
          //          .getSystemService(Context.CONNECTIVITY_SERVICE);
            //NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //if (networkInfo != null) {
             //   return networkInfo.isAvailable();
           // }
        //}
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.user_welcome);
        //检测网络状态
        isWebConnected = isNetworkConnected(MainActivity.this);
        if (isWebConnected) {
            if (userCurState == 0)//user didnot login
            {
                setContentView(R.layout.user_login);
                userAccount = (EditText) findViewById(R.id.user_account);
                userPassword = (EditText) findViewById(R.id.user_password);
                userHeader=(ImageView)findViewById(R.id.user_header);

                //从本地获取用户头像 未写
                //if(loginUser.getHeader()!=null)
                  //  userHeader.setImageDrawable(loginUser.getHeader());

                logIn = (Button) findViewById(R.id.login_in);
                register = (Button) findViewById(R.id.register);
                if (isUserHeaderSetted) {
                    //从本地文件系统中取出用户头像，将其替换
                    // Drawable localDrawable=getLocalDrawable();
                    // ImageView userHeader=(ImageView)findViewById(R.id.user_header);
                    // userHeader.setImageDrawable(localDrawable);
                }
                logIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(userAccount.getText().toString(),userPassword.getText().toString());
                        loginResult = connectWebClass.testUserAccountAndPassword(serverUrl, userAccount.getText().toString(), userPassword.getText().toString());//需要更改
                        if (loginResult == 0) {

                            loginUser.setAccount(userAccount.getText().toString());
                            loginUser.setPassword(userPassword.getText().toString());
                            loginUser.setHeader(userHeader.getDrawable());
                            userCurState = 1;//修改用户状态，已登录
                            Intent intent = new Intent(MainActivity.this, UserDailyActivity.class);
                            startActivity(intent);

                        } else {
                            if (loginResult == 1)//账号不存在
                            {
                                Toast.makeText(getApplicationContext(), "账号不存在", Toast.LENGTH_LONG).show();
                                userAccount.clearComposingText();//清空账号输入框中文
                            } else {
                                Toast.makeText(getApplicationContext(), "密码不正确", Toast.LENGTH_LONG).show();
                                userPassword.clearComposingText();//清空密码输入框中文
                            }
                        }
                    }
                });
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else//直接切换到主页面
            {
                Intent intent = new Intent(MainActivity.this, UserDailyActivity.class);
                startActivity(intent);
            }
        }
    }

    /*@Override
    protected void onRestart()//注册后回来时，头像已设置，得更改
    {
        if(loginUser.getHeader()!=null);
            //userHeader.setImageDrawable(loginUser.getHeader());

    }*/
}
