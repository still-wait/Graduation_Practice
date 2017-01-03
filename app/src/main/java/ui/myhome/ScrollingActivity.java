package ui.myhome;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ygh.graduation_practice.R;

import java.util.ArrayList;
import java.util.List;

import dialog.setMyhomeDialog;

public class ScrollingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mViewpager;
    private View pager1, pager2;
    private TextView tab1, tab2;
    //个人信息页面listview
    private ListView msgList;
    //个人页面信息数据源
    private List<MyInfo> InfoList = new ArrayList<MyInfo>();
    //viewpager
    private List<View> pagerList;
    private PagerAdapter mPagerAdapter;
    private ArrayList<String> adpter = new ArrayList<>();
    //    private PagerAdapter myPagerAdapter;
    private LayoutInflater mInflater;
    private PagerTabStrip TabStrip;

    private ImageView line_tab; // tab选项卡的下划线
    private int moveOne = 0; // 下划线移动一个选项卡
    private boolean isScrolling = false; // 手指是否在滑动
    private boolean isBackScrolling = false; // 手指离开后的回弹
    private long startTime = 0;
    private long currentTime = 0;
    private SharedPreferences pref;

    private TextView name, sex, age, honesty, character, skill, number;
    private TextView name_data, sex_data, age_data, number_data;
    private ImageView honesty_data, character_data, skill_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_activity_scrolling);

        //页卡下划线初始化
        initLineImage();

        initToolbar();

        //viewpager
        mViewpager = (ViewPager) this.findViewById(R.id.viewpager);
        tab1 = (TextView) findViewById(R.id.tv_tab0);
        tab2 = (TextView) findViewById(R.id.tv_tab1);
        mViewpager.setCurrentItem(0);
        tab1.setTextColor(Color.BLUE);
        tab2.setTextColor(Color.BLACK);

        mViewpager.setOnPageChangeListener(this);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        mInflater = LayoutInflater.from(this);

        pager1 = mInflater.inflate(R.layout.me_pager_1, null);
        pager2 = mInflater.inflate(R.layout.me_pager_2, null);
        name_data= (TextView) findViewById(R.id.name_data);
        sex_data= (TextView) findViewById(R.id.sex_data);
        age_data= (TextView) findViewById(R.id.age_data);
        number_data= (TextView) findViewById(R.id.number_data);

        pagerList = new ArrayList<View>();

        pagerList.add(pager1);
        pagerList.add(pager2);

        mPagerAdapter = new mPagerAdapter(pagerList);
        mViewpager.setAdapter(mPagerAdapter);
        //pager2,我的赛事Listview
        msgList = (ListView) findViewById(R.id.msg_list_1);
        initMyInfo();
        mAdapter adapter = new mAdapter(ScrollingActivity.this, R.layout.me_match_item, InfoList);
//        msgList.setAdapter(adapter);

    }


    public void initToolbar() {
        pref = getSharedPreferences("user", MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        getSupportActionBar().setTitle(pref.getString("name","个人主页"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initLineImage() {
        line_tab = (ImageView) findViewById(R.id.line_tab);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels; /** * 重新设置下划线的宽度 */
        ViewGroup.LayoutParams lp = line_tab.getLayoutParams();
        lp.width = screenW / 2;
        line_tab.setLayoutParams(lp);
        moveOne = lp.width;
    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentTime = System.currentTimeMillis();
        if (isScrolling && (currentTime - startTime > 200)) {
            movePositionX(position, moveOne * positionOffset);
            startTime = currentTime;
        }
        if (isBackScrolling) {
            movePositionX(position);
        }
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tab1.setTextColor(Color.BLUE);
                tab2.setTextColor(Color.BLACK);
                movePositionX(0);
                break;
            case 1:
                tab2.setTextColor(Color.BLUE);
                tab1.setTextColor(Color.BLACK);
                movePositionX(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1:
                isScrolling = true;
                isBackScrolling = false;
                break;
            case 2:
                isBackScrolling = true;
                isScrolling = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tab0:
                mViewpager.setCurrentItem(0);
                break;
            case R.id.tv_tab1:
                mViewpager.setCurrentItem(1);
                break;
            default:
                break;
        }

    }

    /**
     * 下划线跟随手指的滑动而移动
     * * @param toPosition * @param positionOffsetPixels
     */
    private void movePositionX(int toPosition, float positionOffsetPixels) {
        // TODO Auto-generated method stub
        float curTranslationX = line_tab.getTranslationX();
        float toPositionX = moveOne * toPosition + positionOffsetPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(line_tab, "translationX", curTranslationX, toPositionX);
        animator.setDuration(500);
        animator.start();
    }

    private void movePositionX(int toPosition) {
        // TODO Auto-generated method stub
        movePositionX(toPosition, 0);
    }


    //
    private void initMyInfo() {

        MyInfo data_1 = new MyInfo(
                "2016年1月6日", "奥克斯广场", "5人/12人", "");
        InfoList.add(data_1);

        MyInfo data_2 = new MyInfo(
                "2016年5月12日", "华城广场", "5.5人/12人", "");
        InfoList.add(data_2);

        MyInfo data_3 = new MyInfo(
                "2016年6月1日", "四水厂", "12人/12人", "踢乒乓球");
        InfoList.add(data_3);

        MyInfo data_4 = new MyInfo(
                "2016年7月15日", "公司", "6人/12人", "");
        InfoList.add(data_4);

        MyInfo data_5 = new MyInfo(
                "2016年8月1日", "橘子洲", "7人/12人", "队长带球");
        InfoList.add(data_5);

        MyInfo data_6 = new MyInfo(
                "2016月10日1日", "ssss", "9人/12人", "");
        InfoList.add(data_6);

        MyInfo data_7 = new MyInfo(
                "2017日1月1日", "湘江", "12人/12人", "自带游泳圈");
        InfoList.add(data_7);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         if(id == R.id.action_set){
             new setMyhomeDialog().show(getSupportFragmentManager(), setMyhomeDialog.class.getName());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    setMyhomeDialog.NewMyhomeDialogListener listener=new setMyhomeDialog.NewMyhomeDialogListener() {
        @Override
        public void getNameAddr(String username, String sex, String age, String tell) {
            name_data.setText(username);
            sex_data.setText(sex);
            age_data.setText(age);
            number_data.setText(tell);
        }

        @Override
        public void onCancel() {

        }
    };


}
