package loading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.example.ygh.graduation_practice.MainActivity;
import com.example.ygh.graduation_practice.R;



/**
 * Created by Y-GH on 2016/6/7.
 */
public class Loading extends FragmentActivity {
    private static final int GOTO_MAIN_ACTIVITY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_launch_page);
        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 3000);

    }


    private Handler mHandler = new Handler() {
        @SuppressLint("LongLogTag")
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    Intent intent2 = new Intent(Loading.this, MainActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
            }
        };
    };


}
