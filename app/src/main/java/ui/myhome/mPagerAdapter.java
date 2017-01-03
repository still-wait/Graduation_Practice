package ui.myhome;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class mPagerAdapter extends PagerAdapter {

    private List<View> pagerList=new ArrayList<>();
    public mPagerAdapter(List<View> pagerList) {
        this.pagerList = pagerList; }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
        container.removeView(pagerList.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pagerList.get(position));
        return pagerList.get(position);
    }

    @Override
    public int getCount() {
        return pagerList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg2) {
        return arg0==arg2;
    }

}
