package com.baronzhang.ipc.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baronzhang.ipc.Book;
import com.baronzhang.ipc.R;
import com.baronzhang.ipc.server.IBookManager;
import com.baronzhang.ipc.server.RemoteService;
import com.baronzhang.ipc.server.Stub;

import java.util.List;

/**
 * 客户端进程
 */
public class ClientActivity extends AppCompatActivity {

    private IBookManager mIBookManager;
    private boolean isConnection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnection) {
                    attemptToBindService();
                    return;
                }

                if (mIBookManager == null)
                    return;

                try {
                    Book book = new Book();
                    book.setPrice(101);
                    book.setName("编码");
                    mIBookManager.addBook(book);

                    Log.d("kevin", mIBookManager.getBooks().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void attemptToBindService() {

        Intent intent = new Intent(this, RemoteService.class);
        intent.setAction("com.baronzhang.ipc.server");
        Log.d("kevin", "ClientActivity#attemptToBindService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        /**
         * 服务绑定成功
         * @param service 如果是同进程，这里返回的是RemoteService的匿名内部类Stub。
         *                如果是不同的进程，这里返回的是Binderproxy。
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnection = true;
            Log.d("kevin", "ClientActivity#onServiceConnected service:" + service);
            mIBookManager = Stub.asInterface(service);//mIBookManager 得到的是 Proxy
            if (mIBookManager != null) {
                try {
                    List<Book> books = mIBookManager.getBooks();
                    Log.d("kevin", "ClientActivity#onServiceConnected#books=" + books.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Log.d("kevin", "ClientActivity#onServiceConnected end");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnection = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("kevin", "ClientActivity#onStart");
        if (!isConnection) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isConnection) {
            unbindService(serviceConnection);
        }
    }
}
