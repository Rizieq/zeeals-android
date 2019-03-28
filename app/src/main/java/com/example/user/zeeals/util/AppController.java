package com.example.user.zeeals.util;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
    private final String TAG = AppController.class.getSimpleName();
    private static AppController instance; //inisialisasi objek
    RequestQueue mRequestQueue; //untuk request

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static synchronized AppController getInstance()
    {
        return instance;
    }

    private RequestQueue getRequestQueue()
    {
        if(mRequestQueue==null)
        {
            mRequestQueue= Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    //melakukan request apabila kosong
    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    //melakukan request
    public <T> void addToRequestQueue (Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    //Cancell Request
    public void cancelAllRequest(Object req)
    {
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(req);
        }
    }

}
