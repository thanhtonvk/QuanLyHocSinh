package com.example.quanlyhocsinh.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.quanlyhocsinh.Entity.HocSinh;

import java.util.ArrayList;
import java.util.List;

public class DBContext extends SQLiteOpenHelper {
    public DBContext(@Nullable Context context) {
        super(context, "database.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table HocSinh(" +
                "ID integer  primary key autoincrement," +
                "HoTen nvarchar(50) not null," +
                "DiaChi nvarchar(100) not  null," +
                "NgaySinh varchar(20)," +
                "GioiTinh nvarchar(4)," +
                "HinhAnh blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<HocSinh> getListHocSinh() {
        List<HocSinh> hocSinhs = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from HocSinh", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HocSinh hocSinh = new HocSinh(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getBlob(5));
            hocSinhs.add(hocSinh);
            cursor.moveToNext();
        }
        database.close();
        return hocSinhs;
    }

    public void addHocSinh(HocSinh hocSinh) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HoTen", hocSinh.getHoten());
        values.put("DiaChi", hocSinh.getDiachi());
        values.put("NgaySinh", hocSinh.getNgaysinh());
        values.put("GioiTinh", hocSinh.getGioitinh());
        values.put("HinhAnh", hocSinh.getHinhanh());
        database.insert("HocSinh", null, values);
        database.close();
    }
}
