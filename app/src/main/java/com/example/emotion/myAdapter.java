package com.example.emotion;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifAnimationMetaData;
import pl.droidsonroids.gif.GifDecoder;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifDrawableBuilder;
import pl.droidsonroids.gif.GifImageView;

public class myAdapter extends BaseAdapter {
    private static final String TAG = "test";
    private ArrayList<Image> list;
    private Context mContext;
    private LayoutInflater inflater;
    public myAdapter(ArrayList<Image> list, Context mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    public void setList(ArrayList<Image> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(list==null)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.show, null);
            holder = new ViewHolder();
            holder.img=(GifImageView) convertView.findViewById(R.id.img) ;
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.type=(TextView)convertView.findViewById(R.id.type);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.type.setText(list.get(position).getType());
        String path=list.get(position).getImage_path();
        File file = new File(path);
        if(file.getName().split("\\.")[1].toLowerCase().equals("gif")){
            try {
                GifDrawable gifFromFile = new GifDrawable(file);
                holder.img.setImageDrawable(gifFromFile);
            } catch (IOException e) {
            }
        }
        else{
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            holder.img.setImageBitmap(bitmap);
        }
        return convertView;
    }
    public class ViewHolder{
        GifImageView img;
        TextView name;
        TextView type;
    }

}
