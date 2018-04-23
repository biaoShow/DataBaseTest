package com.example.biao.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by biao on 2018/4/23.
 */

public class DatabaseProvider extends ContentProvider {

    private static final int BOOK_DIR = 0;
    private static final int BOOK_ITEM = 1;
    private static final int CATEGORY_DIR = 2;
    private static final int CATEGORY_ITEM = 3;
    private static final String AUTHORITY = "com.example.biao.databasetest.provider";
    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    private MyDataBaseHelper myDataBaseHelper;

    @Override
    public boolean onCreate() {
        myDataBaseHelper = new MyDataBaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //查询数据
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                cursor = sqLiteDatabase.query("Book", projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = sqLiteDatabase.query("Book", projection, "id=?",
                        new String[]{bookId}, null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = sqLiteDatabase.query("category", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = sqLiteDatabase.query("category", projection, "id=?", new String[]{categoryId}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.biao.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.biao.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.biao.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.biao.databasetest.provider.category";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = sqLiteDatabase.insert("Book", null, values);
                uriReturn = Uri.parse("content:" + AUTHORITY + "/book/" + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                Long newCategory = sqLiteDatabase.insert("category", null, values);
                uriReturn = Uri.parse("content:" + AUTHORITY + "/category" + newCategory);
                break;
            default:
                break;
        }

        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deletedRows = sqLiteDatabase.delete("Book", selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deletedRows = sqLiteDatabase.delete("Book", "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deletedRows = sqLiteDatabase.delete("Category", selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deletedRows = sqLiteDatabase.delete("Category", "id=?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows = sqLiteDatabase.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = sqLiteDatabase.update("Book",values,"id=?",new String[] {bookId});
                break;
            case CATEGORY_DIR:
                updateRows = sqLiteDatabase.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = sqLiteDatabase.update("Category",values,"id=?",new String[] {categoryId});
                break;
                default:
                    break;
        }
        return updateRows;
    }
}
