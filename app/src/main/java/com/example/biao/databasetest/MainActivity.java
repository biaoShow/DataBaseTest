package com.example.biao.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn_create,btn_insert,btn_update,btn_delete,btn_query,btn_replace;
    private MyDataBaseHelper myDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_create = findViewById(R.id.btn_create);
        btn_insert = findViewById(R.id.btn_insert);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_query = findViewById(R.id.btn_query);
        btn_replace = findViewById(R.id.btn_replace);
        myDataBaseHelper = new MyDataBaseHelper(this,"BookStore.db",null,2);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDataBaseHelper.getWritableDatabase();
            }
        });
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("name","The Da Vinci Code");
                contentValues.put("author","Dan Brown");
                contentValues.put("price","16.96");
                contentValues.put("pages","454");
                sqLiteDatabase.insert("Book",null,contentValues);
                contentValues.clear();
                contentValues.put("name","nima");
                contentValues.put("author","liyuanbiao");
                contentValues.put("price","999");
                contentValues.put("pages","1");
                sqLiteDatabase.insert("Book",null,contentValues);
                Toast.makeText(MainActivity.this,"添加成功！",Toast.LENGTH_SHORT).show();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("price","10.99");
                sqLiteDatabase.update("Book",contentValues,"name=?",
                        new String[] {"The Da Vinci Code"});
                Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
                sqLiteDatabase.delete("Book","pages>?",new String[] {"400"});
                Toast.makeText(MainActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
            }
        });
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.query("Book",null,null,null,
                        null,null,null);
                while (cursor.moveToNext()){
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        String data = "name:"+name+"  author:"+author+"  pages:"+
                                String.valueOf(pages)+"  price:"+String.valueOf(price);
                        Toast.makeText(MainActivity.this,data,Toast.LENGTH_SHORT).show();
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
        btn_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
                sqLiteDatabase.beginTransaction();
                try{
                    sqLiteDatabase.delete("Book",null,null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name","liyuanbiaozhishuo");
                    contentValues.put("author","liyuanbiao");
                    contentValues.put("price","1999");
                    contentValues.put("pages","555");
                    sqLiteDatabase.insert("Book",null,contentValues);
                    sqLiteDatabase.setTransactionSuccessful();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    sqLiteDatabase.endTransaction();
                    Toast.makeText(MainActivity.this,"替换成功！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
