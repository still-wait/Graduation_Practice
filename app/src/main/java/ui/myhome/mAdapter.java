package ui.myhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ygh.graduation_practice.R;

import java.util.List;


public class mAdapter extends ArrayAdapter<MyInfo> {

    private int resorceId;
    public mAdapter(Context context, int textViewResourceId, List<MyInfo> objects){
        super(context,textViewResourceId,objects);
        resorceId=textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyInfo myInfo=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView ==null){
            view= LayoutInflater.from(getContext()).inflate(resorceId,null);
            viewHolder=new ViewHolder();
//            viewHolder.msg_photo=(XCRoundImageView) view.findViewById(R.id.photo);
            viewHolder.msg_time=(TextView)view.findViewById(R.id.msg_tv_time);
            viewHolder.msg_location=(TextView)view.findViewById(R.id.msg_tv_location);
            viewHolder.msg_count=(TextView)view.findViewById(R.id.msg_tv_number) ;
            viewHolder.msg_remark=(TextView)view.findViewById(R.id.msg_tv_remark) ;
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
//        if(myInfo.getImage()!=null){
//            viewHolder.msg_photo.setImageDrawable(myInfo.getImage());
//        }
//        viewHolder.msg_photo.setType(XCRoundImageView.TYPE_ROUND);
//        viewHolder.msg_photo.setRoundBorderRadius(20);

        viewHolder.msg_time.setText(myInfo.getTime());
        viewHolder.msg_location.setText(myInfo.getLocation());
        viewHolder.msg_count.setText(myInfo.getCount());
        viewHolder.msg_remark.setText(myInfo.getRemark());

        return view;
    }
    class ViewHolder{
//        XCRoundImageView msg_photo;
        TextView msg_time;
        TextView msg_location;
        TextView msg_count;
        TextView msg_remark;
    }
}
