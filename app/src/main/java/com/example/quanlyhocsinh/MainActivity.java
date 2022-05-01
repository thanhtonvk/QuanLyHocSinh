package com.example.quanlyhocsinh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlyhocsinh.Database.DBContext;
import com.example.quanlyhocsinh.Entity.HocSinh;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView auto_timkiem;
    EditText edt_hoten, edt_diachi, edt_ngaysinh, edt_gioitinh;
    ImageView img_avatar;
    Button btn_them;
    ListView lv_hocsinh;

    List<HocSinh> hocSinhList = new ArrayList<>();
    ArrayAdapter<HocSinh> adapter;
    DBContext db;

    private static final int REQUEST_GALERY = 327;
    HocSinh hocSinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auto_timkiem = findViewById(R.id.auto_timkiem);
        edt_hoten = findViewById(R.id.edt_ten);
        edt_diachi = findViewById(R.id.edt_diachi);
        edt_ngaysinh = findViewById(R.id.edt_ngaysinh);
        edt_gioitinh = findViewById(R.id.edt_gioitinh);
        img_avatar = findViewById(R.id.img_avatar);
        btn_them = findViewById(R.id.btn_them);
        lv_hocsinh = findViewById(R.id.lv_sinhvien);
        db = new DBContext(getApplicationContext());
        //do du lieu hoc sinh ra listview
        loadHocSinh();
        hocSinh = new HocSinh();
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mở ảnh từ thư viện

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALERY);
            }
        });
        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hocSinh.setHoten(edt_hoten.getText().toString());
                hocSinh.setDiachi(edt_diachi.getText().toString());
                hocSinh.setGioitinh(edt_gioitinh.getText().toString());
                hocSinh.setNgaysinh(edt_ngaysinh.getText().toString());
                db.addHocSinh(hocSinh);
                hocSinh = new HocSinh();
                loadHocSinh();
            }
        });
        //set su kien khi chon vao 1 dong trong listview

        lv_hocsinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get doi tuong hoc sinh khi click vao
                HocSinh rs = hocSinhList.get(i);
                //load du lieu ra editext va image view
                edt_hoten.setText(rs.getHoten());
                edt_diachi.setText(rs.getDiachi());
                edt_ngaysinh.setText(rs.getNgaysinh());
                edt_gioitinh.setText(rs.getGioitinh());
                byte[] anh = rs.getHinhanh();
                Bitmap bitmap = BitmapUtils.getImage(anh);
                img_avatar.setImageBitmap(bitmap);
            }
        });


    }

    //load danh sach hoc sinh trong database
    private void loadHocSinh() {
        hocSinhList = db.getListHocSinh();
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, hocSinhList);
        lv_hocsinh.setAdapter(adapter);
        auto_timkiem.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALERY) {
            Uri uri = data.getData();
            try {
                InputStream stream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                img_avatar.setImageBitmap(bitmap);
                //giảm dung lượng
                Bitmap resize = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.2), (int) (bitmap.getHeight() * 0.2), true);
                byte[] anh = BitmapUtils.getBytes(resize);
                hocSinh.setHinhanh(anh);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}