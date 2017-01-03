package com.example.ygh.graduation_practice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import dialog.LoginDialog;
import dialog.NewAnswerDialog;
import dialog.NewMatchDialog;
import dialog.NewQuestionDialog;
import storage.CurrentFragment;
import storage.CurrentQuestion;
import storage.CurrentUser;
import transfer.UserTransfer;
import ui.main.AnswerFragment;
import ui.main.HomeFragment;
import ui.me.LogoutDialogFragment;
import ui.myhome.ScrollingActivity;

import static com.example.ygh.graduation_practice.R.menu.main;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewQuestionDialog.NewQuestionDialogCreater, NewAnswerDialog.NewAnswerDialogCreater {

    FragmentManager fragmentManager;
    private TextView name;
    private SharedPreferences pref;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCurrentFragmentName().equals(HomeFragment.class.getName())) {
                    new NewMatchDialog().show(getSupportFragmentManager(), NewMatchDialog.class.getName());
                }
                if (getCurrentFragmentName().equals(AnswerFragment.class.getName())) {
                    new NewAnswerDialog().show(getSupportFragmentManager(), NewAnswerDialog.class.getName() + CurrentQuestion.getInstance().id);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        if (name.getText().toString().equals("登录")) {
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showLoginDialog(pref.getString("username", ""));
                        }
                    }, 500);
                    return;

                }
            });
        }
    }


//    @Override
//    public LoginDialog.LoginDialogListener getLoginDialogListener() {
//        return new LoginDialog.LoginDialogListener() {
//            @Override
//            public void onLoginSuccess(UserTransfer userTransfer) {
//                MainActivity.this.onLoginSuccess(userTransfer);
//            }
//
//            @Override
//            public void onRegisterSuccess(String username, String password) {
//
//            }
//
//            @Override
//            public void onDialogCancel() {
//
//            }
//        };
//    }

    public void onLoginSuccess(UserTransfer userTransfer) {
        CurrentUser.getInstance().storage(userTransfer);
        // 数据库管理
        // 启动主活动
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                JActivityManager.getInstance().closeAllActivity();
//                MainActivity.actionStart(LaunchPageActivity.this);
            }
        }, 1500);
    }


    private void initView() {
        pref = getSharedPreferences("user", MODE_PRIVATE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        name = (TextView) headerView.findViewById(R.id.nav_username);
        if(!pref.getString("username","").equals("")){
            name.setText(pref.getString("name", "未设置"));
        }

        /**
         * 加载数据
         */
        try {
            setFragment((Fragment) CurrentFragment.getInstance().getSavedFragmentClass().newInstance(), CurrentFragment.getInstance().getSavedFragmentClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
            setFragment(new HomeFragment(), HomeFragment.class.getName());
        }
    }

    public String getCurrentFragmentName() {
        return CurrentFragment.getInstance().getSavedFragmentClass().getName();
    }

    /**
     * 登录
     *
     * @param defaultUsername
     */
    private void showLoginDialog(String defaultUsername) {
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.setDialogListener(dialogListener);
        loginDialog.show(getSupportFragmentManager(), defaultUsername);
    }

    LoginDialog.LoginDialogListener dialogListener = new LoginDialog.LoginDialogListener() {

        @Override
        public void getNameAddr(String username) {
            Log.e("2222登录成功", "   " + username);
            name.setText(username);
        }



    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setFragment(Fragment fragment, String tag) {
        CurrentFragment.getInstance().storage(fragment);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment, tag).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myhome) {
            Intent intent=new Intent(MainActivity.this, ScrollingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mymatch) {

        } else if (id == R.id.nav_team) {

        } else if (id == R.id.nav_mymessage) {

        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_exit) {
            LogoutDialogFragment fragment = new LogoutDialogFragment();
            fragment.show(getFragmentManager(), null);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public NewQuestionDialog.NewQuestionDialogListener getNewQuestionDialogListener() {
        return new NewQuestionDialog.NewQuestionDialogListener() {
            @Override
            public void onSuccess() {
                ((CommitSuccessCallBack) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName())).onCommitSuccess();
            }

            @Override
            public void onCancel() {

            }
        };
    }

    @Override
    public NewAnswerDialog.NewAnswerDialogListener getNewAnswerDialogListener() {
        return new NewAnswerDialog.NewAnswerDialogListener() {
            @Override
            public void onSuccess() {
                ((CommitSuccessCallBack) getSupportFragmentManager().findFragmentByTag(AnswerFragment.class.getName() + CurrentQuestion.getInstance().id)).onCommitSuccess();
            }

            @Override
            public void onCancel() {

            }
        };
    }

    public interface CommitSuccessCallBack {
        void onCommitSuccess();
    }
}
