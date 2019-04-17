package com.example.administrator.watchoutapplication;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * 检测APP是否退出？
     */
    String m_sMonitorAppName = "com.hstt.foshanrke"; //要监测的App的包名
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyThread startThread = new MyThread();//检测APP是否退出
        new Thread(startThread ).start(); //启动线程//
    }



    class MyThread  implements Runnable {
        public void  run(){
            try {
                Thread.sleep(300000); //延时1min
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(true){

                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> runningTasks = manager.getRunningAppProcesses();

                // 获得当前最顶端的任务栈，即前台任务栈
                ActivityManager.RunningAppProcessInfo runningTaskInfo = runningTasks.get(0);
                String packageName = runningTaskInfo.processName.toString();

                if(!packageName.equals(m_sMonitorAppName)){

                    PackageManager packageManager = getPackageManager();
                    PackageInfo packageInfo = null;
                    //在这里，该App虽然没在前台运行，也有可能在后台运行（未被结束），
                    //为了更合理，应该先结束掉，但是注释的方法总是崩溃..........
                    //android.os.Process.killProcess(runningTaskInfo.pid); //结束进程

                    try {
                        packageInfo = getPackageManager().getPackageInfo(packageName, 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    if(packageInfo != null){
                        Intent intent = packageManager.getLaunchIntentForPackage(m_sMonitorAppName);
                        startActivity(intent);//启动App
                    }
                }
                try {
                    Thread.sleep(10000); //延时10s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
