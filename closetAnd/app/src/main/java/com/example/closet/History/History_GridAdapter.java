package com.example.closet.History;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.closet.Clothes.UrlToBitmap;
import com.example.closet.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

class History_GridAdapter extends BaseAdapter {

    Context context = null;
    private LayoutInflater inflater;
    private int layout;

    ArrayList<String> arrayTextList = null;
    ArrayList<URL> photoUrls;
    ArrayList<Bitmap> photoBitmap = new ArrayList<>();
    UrlToBitmap urlToBitmap;

    public History_GridAdapter(Context context,  ArrayList<String> arrayTextList, ArrayList<URL> photoUrls) {
        this.context = context;
        this.arrayTextList = arrayTextList;
        this.photoUrls = photoUrls;
        Log.d("Log_dasagagadg", String.valueOf(this.photoUrls.get(0)));

        urlToBitmap = new UrlToBitmap(photoUrls);
        urlToBitmap.execute();
        try {
            photoBitmap = urlToBitmap.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /*
    public int getCount() { return (null != imageIDs) ? imageIDs.length : 0; }

    public Object getItem(int position) {
        return (null != imageIDs) ? imageIDs[position] : 0;
    }

    public long getItemId(int position) {
        return position;
    }
    */

    public int getCount() {
        return photoUrls.size();
    }

    public Object getItem(int i) {
        return photoUrls.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View gridView;

        if (view == null) {
            view = inflater.inflate(layout, viewGroup, false);
            viewHolder = new History_GridAdapter.ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.history_iv);
            viewHolder.textView = (TextView) view.findViewById(R.id.history_tv);
            view.setTag(viewHolder);
        } else
            viewHolder = (History_GridAdapter.ViewHolder) view.getTag();
        viewHolder.imageView.setImageBitmap(photoBitmap.get(i));
        viewHolder.textView.setText(arrayTextList.get(i));

        // 사진 항목들의 클릭을 처리하는 ImageClickListener 객체를 정의합니다.
        // 그리고 그것을 ImageView의 클릭 리스너로 설정합니다.
        History_GridClickListener imageViewClickListener = new History_GridClickListener(context, photoUrls.get(i));
        viewHolder.imageView.setOnClickListener(imageViewClickListener);

        return view;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }

}