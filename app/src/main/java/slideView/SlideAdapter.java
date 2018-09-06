package slideView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinheng on 2017/11/13.
 * describe：
 */

public class SlideAdapter<T>{
    private final LayoutInflater mInflater;
    private List<T> mObjects;
    public SlideAdapter(Context context, ArrayList<T> list){
        mObjects=list;
        //ListView
        //mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater = LayoutInflater.from(context);
    }
    public int getCount() {
        return mObjects!=null?mObjects.size():0;
    }

    public T getItem(int position) {
        return mObjects!=null?mObjects.get(position):null;
    }

    public long getItemId(int position) {
        return position;
    }
    public int getItemViewType(int position){
        return 0;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=new TextView(mInflater.getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                convertView.setBackground(new ColorDrawable(Color.WHITE));
            }
            ((TextView)convertView).setGravity(Gravity.CENTER);
            //convertView.setLayoutParams(new SlideView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1));
            convertView.setLayoutParams(new SlideView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50,1));
        }
        Log.e("TAG_adapter","viewTyppe="+((SlideView.LayoutParams)convertView.getLayoutParams()).viewType+", "+position);
        if(convertView instanceof TextView){
            ((TextView) convertView).setText(mObjects.get(position)+"");
            if(position%2==0){
                ((TextView) convertView).setTextColor(Color.RED);
            }else {
                ((TextView) convertView).setTextColor(Color.GREEN);
            }
        }
        return convertView;
    }

}
