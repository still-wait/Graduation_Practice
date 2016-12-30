package com.example.ygh.graduation_practice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dialog.NewAnswerDialog;
import dialog.NewMatchDialog;
import dialog.NewQuestionDialog;
import storage.CurrentFragment;
import storage.CurrentQuestion;
import ui.main.AnswerFragment;
import ui.main.HomeFragment;

import static com.example.ygh.graduation_practice.R.menu.main;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewQuestionDialog.NewQuestionDialogCreater, NewAnswerDialog.NewAnswerDialogCreater {

    FragmentManager fragmentManager;

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
                    new NewMatchDialog().show(getSupportFragmentManager(), NewQuestionDialog.class.getName());
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView() {
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
            // Handle the camera action
        } else if (id == R.id.nav_mymatch) {

        } else if (id == R.id.nav_team) {

        } else if (id == R.id.nav_mymessage) {

        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_exit) {

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
