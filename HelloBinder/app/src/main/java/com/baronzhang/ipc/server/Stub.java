package com.baronzhang.ipc.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.baronzhang.ipc.Book;
import com.baronzhang.ipc.proxy.Proxy;

import java.util.List;

/**
 * IBinder 是一个接口，代表了一种跨进程通信的能力。只要实现了这个借口，这个对象就能跨进程传输。
 * <p>
 * Binder: Java 层的 Binder 类，代表的其实就是 Binder 本地对象。BinderProxy 类是 Binder 类的一个内部类，
 * BinderProxy 代表远程进程的 Binder 对象的本地代理；这两个类都继承自 IBinder, 因而都具有跨进程传输的能力；
 * 实际上，在跨越进程的时候，Binder 驱动会自动完成这两个对象的转换。
 * <p>
 * Stub : AIDL 的时候，编译工具会给我们生成一个名为 Stub 的静态内部类；
 * 这个类继承了 Binder, 说明它是一个 Binder 本地对象，它实现了 IInterface 接口，表明它具有 Server 承诺给 Client 的能力；
 * Stub 是一个抽象类，具体的 IInterface 的相关实现需要开发者自己实现。
 */
public abstract class Stub extends Binder implements IBookManager {

    private static final String DESCRIPTOR = "com.baronzhang.ipc.server.BookManager";

    public Stub() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder binder) {
        if (binder == null)
            return null;
        IInterface iin = binder.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof IBookManager)
            return (IBookManager) iin;
        return new Proxy(binder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {

            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;

            case TRANSAVTION_getBooks:
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBooks();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;

            case TRANSAVTION_addBook:
                data.enforceInterface(DESCRIPTOR);
                Book arg0 = null;
                if (data.readInt() != 0) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;

        }
        return super.onTransact(code, data, reply, flags);
    }

    public static final int TRANSAVTION_getBooks = IBinder.FIRST_CALL_TRANSACTION;
    public static final int TRANSAVTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;
}
