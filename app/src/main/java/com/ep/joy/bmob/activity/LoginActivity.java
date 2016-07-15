package com.ep.joy.bmob.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ep.joy.bmob.R;
import com.ep.joy.bmob.base.BaseActivity;
import com.ep.joy.bmob.bean.MyUser;
import com.ep.joy.bmob.weight.ClearEditText;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobUser;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * author   Joy
 * Date:  2016/7/6.
 * version:  V1.0
 * Description:
 */
public class LoginActivity extends BaseActivity {

    private ClearEditText userEt;
    private ClearEditText pwdEt;
    private TextView forgetpwdTv;
    private Button loginBtn;
    private Context mContext;
    private MyUser user;

    @Override
    protected int setContentView() {
        mContext = this;
        return R.layout.activity_login;
    }


    protected void initView() {
        userEt = (ClearEditText) findViewById(R.id.user_et);
        pwdEt = (ClearEditText) findViewById(R.id.pwd_et);
        forgetpwdTv = (TextView) findViewById(R.id.forgetpwd_tv);
        loginBtn = (Button) findViewById(R.id.login_btn);
         user = BmobUser.getCurrentUser(MyUser.class);
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        validateForm();
        RxView.clicks(loginBtn)
                .debounce(500, TimeUnit.MICROSECONDS)
                .concatMap(new Func1<Void, Observable<MyUser>>() {
                               @Override
                               public Observable<MyUser> call(Void v) {
                                   user = new MyUser();
                                   user.setUsername(userEt.getText().toString());
                                   user.setPassword(pwdEt.getText().toString());
                                   Observable<MyUser> logiObservable = user.loginObservable(MyUser.class);
                                   return logiObservable;
                               }
                           }
                )
                .compose(this.<MyUser>bindToLifecycle())
                .subscribe(new Action1<MyUser>() {
                    @Override
                    public void call(MyUser myUser) {
                        toast(myUser.getUsername() + "---------" + myUser.getObjectId());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        toast(throwable.getMessage());
                    }
                });


        forgetpwdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(LoginActivity.this, R.style.Dialog);
                View v = LayoutInflater.from(LoginActivity.this).inflate(R.layout.register_dialog, null);
                dialog.setContentView(v);
                dialog.show();
                final TextInputEditText Useret = (TextInputEditText) v.findViewById(R.id.user_dia);
                final TextInputEditText Pwdet = (TextInputEditText) v.findViewById(R.id.pwd_dia);

                v.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button saveBtn = (Button) v.findViewById(R.id.ok);
                RxView.clicks(saveBtn)
                        .debounce(500, TimeUnit.MICROSECONDS)
                        .concatMap(new Func1<Void, Observable<MyUser>>() {
                            @Override
                            public Observable<MyUser> call(Void aVoid) {
                                user.setPassword(Pwdet.getText().toString());
                                user.setUsername(Useret.getText().toString());
                                Observable<MyUser> registerObservable = user.signUpObservable(MyUser.class);
                                return registerObservable;
                            }
                        })
                        .compose(LoginActivity.this.<MyUser>bindToLifecycle())
                        .subscribe(new Action1<MyUser>() {
                            @Override
                            public void call(MyUser myUser) {
                                toast("注册成功");
                                dialog.dismiss();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                toast("注册失败：" + throwable.getMessage());
                                dialog.dismiss();
                            }
                        });

            }
        });


    }


    private void validateForm() {
        Observable<CharSequence> ObservableUser = RxTextView.textChanges(userEt).compose(this.<CharSequence>bindToLifecycle());
        Observable<CharSequence> ObservablePassword = RxTextView.textChanges(pwdEt).compose(this.<CharSequence>bindToLifecycle());

        Observable.combineLatest(ObservableUser, ObservablePassword, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                return isUserValid(charSequence.toString()) && isPasswordValid(charSequence2.toString());
            }
        })
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        loginBtn.setEnabled(aBoolean);
                        if (aBoolean) {
                            loginBtn.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            loginBtn.setTextColor(getResources().getColor(R.color.login_false));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        new Exception(throwable);
                    }
                });
    }


    private boolean isUserValid(String email) {
        //TODO: Replace this with your own logic
        return !email.isEmpty() && email.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }


}
