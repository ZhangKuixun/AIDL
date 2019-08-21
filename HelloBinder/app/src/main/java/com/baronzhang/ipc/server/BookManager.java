package com.baronzhang.ipc.server;

import android.os.IInterface;
import android.os.RemoteException;

import com.baronzhang.ipc.Book;

import java.util.List;

/**
 * 这个类用来定义服务端 RemoteService 具备什么样的能力
 * <p>
 * IInterface 代表的就是 Server 进程对象具备什么样的能力（能提供哪些方法，其实对应的就是 AIDL 文件中定义的接口）
 */
public interface BookManager extends IInterface {

    List<Book> getBooks() throws RemoteException;

    void addBook(Book book) throws RemoteException;
}
