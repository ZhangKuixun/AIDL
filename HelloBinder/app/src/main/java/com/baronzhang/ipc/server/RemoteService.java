package com.baronzhang.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.baronzhang.ipc.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务端进程，提供服务
 */
public class RemoteService extends Service {

    private List<Book> books = new ArrayList<>();

    public RemoteService() {
        Log.d("kevin", "new RemoteService class");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("kevin", "RemoteService#onCreate");
        Book book = new Book();
        book.setName("三体");
        book.setPrice(88);
        books.add(book);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("kevin", "RemoteService#onBind");
        return bookManager;
    }

    private final Stub bookManager = new Stub() {

        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                Log.d("kevin", "RemoteService#Stub#getBooks");
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                Log.d("kevin", "RemoteService#Stub#addBook");
                if (books == null) {
                    books = new ArrayList<>();
                }

                if (book == null)
                    return;

                book.setPrice(book.getPrice() * 2);
                books.add(book);

                Log.e("kevin", "RemoteService#Stub#books=" + book.toString());
            }
        }
    };
}
