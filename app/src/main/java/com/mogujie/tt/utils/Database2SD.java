package com.mogujie.tt.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 将应用中的数据库拷贝到sd卡中
 * Created by home on 2016/5/12.
 */
public class Database2SD {
    public static void copyDatabase2SD(Context context, String databaseName){
        String fileName="/data/data/"+context.getPackageName()+"/databases/"+databaseName;

        File file=new File("/data/data/"+context.getPackageName());
        for(String path:file.list()){
            Log.e("Database2SD",path);
        }
        File databaseFile=new File(fileName);

        try {
            FileInputStream fis = new FileInputStream(databaseFile);
            FileChannel inChannel=fis.getChannel();

            FileOutputStream fos=new FileOutputStream(new File("/sdcard/teamtalk/"+databaseName));
            FileChannel outChannel=fos.getChannel();
            outChannel.transferFrom(inChannel, 0, inChannel.size());

            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
