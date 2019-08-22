package com.baronzhang.ipc.proxy;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.baronzhang.ipc.Book;
import com.baronzhang.ipc.server.IBookManager;
import com.baronzhang.ipc.server.Stub;

import java.util.List;

/**
 *
 */
public class Proxy implements IBookManager {

    private static final String DESCRIPTOR = "com.baronzhang.ipc.server.BookManager";

    private IBinder remote;

    public Proxy(IBinder remote) {
        Log.d("kevin", "new Proxy class");
        this.remote = remote;
    }

    public String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public List<Book> getBooks() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();
        List<Book> result;
        Log.d("kevin", "Proxy#getBooks");

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            remote.transact(Stub.TRANSAVTION_getBooks, data, replay, 0);
            replay.readException();
            result = replay.createTypedArrayList(Book.CREATOR);
        } finally {
            replay.recycle();
            data.recycle();
        }
        Log.d("kevin", "Proxy#getBooks end");
        return result;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();
        Log.d("kevin", "Proxy#addBook");

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            if (book != null) {
                data.writeInt(1);
                book.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            remote.transact(Stub.TRANSAVTION_addBook, data, replay, 0);
            replay.readException();
        } finally {
            replay.recycle();
            data.recycle();
        }
        Log.d("kevin", "Proxy#addBook");
    }

    @Override
    public IBinder asBinder() {
        return remote;
    }
}
