package com.example.emotion;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class emotion_show extends AppCompatActivity {
    String app_path;
    String TAG="test";
    private SQLiteDatabase db;
    String[] type={"兔斯基","AC娘","暴走","脆皮鸡","恶搞兔","金馆长"};
    GridView gridView;
    ArrayList<Image> list=new ArrayList<Image>();
    ArrayList<Image> tmp=new ArrayList<Image>();
    GifImageView gif;
    myAdapter adapter=null;
    int select=-1;
    LinearLayout layout;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_show);
        db=openOrCreateDatabase("Data.db3",0,null);
        app_path=getApplicationContext().getFilesDir().getAbsolutePath();
        layout =(LinearLayout) findViewById(R.id.choice);
        gridView=(GridView)findViewById(R.id.gridView);
        gif=(GifImageView)findViewById(R.id.gif);
        gif.setClickable(true);
        gif.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()!=MotionEvent.ACTION_UP)
                    return false;
                if(v.getVisibility()==View.VISIBLE){
                    v.setVisibility(View.GONE);
                }
                return true;
            }
        });
        final Cursor cursor=db.query("emotion",new String[]{"file_path","name","type"},
                null,null,null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            do{
                String p=cursor.getString(0).toString();
                String n=cursor.getString(1).toString();
                String t=type[cursor.getInt(2)];
                Image img=new Image(p,n,t);
                list.add(img);
            }while (cursor.moveToNext());
        }
        tmp= (ArrayList<Image>) list.clone();
        cursor.close();
        adapter=new myAdapter(list,this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path=tmp.get(position).getImage_path();
                File file = new File(path);
                if(file.getName().split("\\.")[1].toLowerCase().equals("gif")){
                    try {
                        GifDrawable gifFromFile = new GifDrawable(file);
                        gif.setImageDrawable(gifFromFile);
                    } catch (IOException e) {
                    }
                }
                else{
                    Bitmap bitmap= BitmapFactory.decodeFile(path);
                    gif.setImageBitmap(bitmap);
                }
                gif.setVisibility(View.VISIBLE);
            }
        });
    }
    public void log(String s){
        Log.d("test",s);
    }
    public void sort(View view){
        int tag=Integer.parseInt(view.getTag().toString());
        if(select==tag){
            view.setBackgroundColor(Color.argb(96,32,32,144));
            tmp= (ArrayList<Image>) list.clone();
            adapter.setList(tmp);
            gridView.setAdapter(adapter);
            select=-1;
            return;
        }
        if(select!=-1){
            TextView tv=(TextView)layout.findViewWithTag(""+select);
            tv.setBackgroundColor(Color.argb(96,32,32,144));
        }
        select=tag;
        view.setBackgroundColor(Color.argb(160,32,32,144));
        tmp.clear();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getType().equals(type[tag])){
                tmp.add(list.get(i));
            }
        }
        adapter.setList(tmp);
        gridView.setAdapter(adapter);
    }
    @Override
    public boolean onTouchEvent(final MotionEvent mv){
        log(""+mv.getX()+" "+mv.getY());
        if(mv.getAction()==MotionEvent.ACTION_UP){
            if(gif.getVisibility()==View.VISIBLE)
                gif.setVisibility(View.GONE);
        }
        return false;
    }
    @Override
    public void onBackPressed(){
        if(gif.getVisibility()==View.VISIBLE)
            gif.setVisibility(View.GONE);
        else {
            finish();
        }
    }
}
