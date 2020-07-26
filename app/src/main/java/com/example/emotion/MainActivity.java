package com.example.emotion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
class Image{
    String image_path;
    String name;
    String type;
    public Image(String path,String n,String t){
        image_path=path;
        name=n;
        type=t;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getImage_path() {
        return image_path;
    }
}
public class MainActivity extends AppCompatActivity {
    String app_path;
    String TAG="test";
    private SQLiteDatabase db;
    String[] type={"tusiji","acniang","baozou","cuipiji","egaotu","jinguanzhang"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=openOrCreateDatabase("Data.db3",0,null);
        app_path=getApplicationContext().getFilesDir().getAbsolutePath();
        try{
            db.execSQL("CREATE TABLE emotion(_id integer primary key autoincrement," +
                    " file_path varchar(64) unique," +
                    " name varchar(64)," +
                    " type int default 0," +
                    " last_test_time timestamp)");
        }catch (Exception ignored){
        }
        try {
            initFile();
        } catch (Exception ignored) {
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle("表情图库");
        actionBar.setSubtitle("-斗图必胜宝典");
    }
    public void initFile() throws IOException {
        for(String s:type) {
            File file=new File(app_path+File.separator+s);
            if(!file.exists())
                file.mkdirs();
        }
        File tag=new File(app_path+"/tag");
        if(tag.exists()){
            return;
        }
        InputStream zip=getAssets().open("emotion.zip");
        ZipInputStream zipInputStream = new ZipInputStream(zip);
        ZipEntry nextEntry = zipInputStream.getNextEntry();
        byte[] buffer = new byte[1024 * 1024];
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (nextEntry != null) {
            // 如果是一个文件夹
            if (nextEntry.isDirectory()) {
                File file = new File(app_path + File.separator + nextEntry.getName());
                if (  !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件那就保存
                File file = new File(app_path + File.separator + nextEntry.getName());
                // 则解压文件
                if ( !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, count);
                    }

                    fos.close();
                }
            }
            //这里很关键循环解读下一个文件
            nextEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        tag.createNewFile();
        initDatabase();
    }
    public void initDatabase(){
        for(int i=0;i<7;i++){
            File file=new File(app_path+File.separator+type[i]);
            File[] pic=file.listFiles();
            for(File j:pic){
                ContentValues values=new ContentValues();
                values.put("file_path",j.getAbsolutePath());
                values.put("name",j.getName().toString().split("\\.")[0]);
                values.put("type",i);
                db.insert("emotion",null,values);
            }
        }
    }
    public void log(String s){
        Log.d("test",s);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.support, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.query:
                intent.setAction("com.example.startact.database");
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
