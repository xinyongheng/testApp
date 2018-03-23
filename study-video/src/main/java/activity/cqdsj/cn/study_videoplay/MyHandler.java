package activity.cqdsj.cn.study_videoplay;

import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by xinheng on 2017/11/27.
 * describe：
 */

public class MyHandler<T> extends Handler {
    private WeakReference<T> reference;
    public MyHandler(T t){
        reference=new WeakReference<T>(t);
    }
    public boolean isEffective(){
        return getT()!=null;
    }
    public T getT(){
       return reference.get();
    }
}
