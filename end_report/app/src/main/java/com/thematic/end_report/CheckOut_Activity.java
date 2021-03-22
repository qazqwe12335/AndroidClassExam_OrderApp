package com.thematic.end_report;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckOut_Activity extends AppCompatActivity {

    static final String db_name = "userinfodb";
    static final String tb_name = "userbasicinfo";

    EditText edit_name, edit_phone, edit_address, edit_email;
    TextView txv_commodity, txv_price, txv_sale;

    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_);

        init();
    }

    private void init() {
        edit_name = findViewById(R.id.check_out_page_name);
        edit_phone = findViewById(R.id.check_out_page_phone);
        edit_address = findViewById(R.id.check_out_page_address);
        edit_email = findViewById(R.id.check_out_page_email);

        txv_commodity = findViewById(R.id.check_out_page_commodity);
        txv_price = findViewById(R.id.check_out_page_price);
        txv_sale = findViewById(R.id.check_out_page_sale);

        txv_commodity.setText(getResources().getString(R.string.check_out_commodity) + (getIntent().getStringExtra("food_name")));
        txv_price.setText(getResources().getString(R.string.check_out_price) + getIntent().getStringExtra("price"));
        String a = getIntent().getStringExtra("price");
        if (Integer.valueOf(a) > 500) {
            int c = (int) (Integer.valueOf(a) * 0.9);
            txv_sale.setText(String.valueOf(c));
        } else {
            txv_sale.setText("");
        }


        db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
        String create = "CREATE TABLE IF NOT EXISTS " +
                tb_name +
                "(name VARCHAR(32), " +
                "phone VARCHAR(32)," +
                "address VARCHAR(32)," +
                "email VARCHAR(32))";
        db.execSQL(create);
    }

    public void check(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.check_out_dialog_title)
                .setPositiveButton(R.string.check_out_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String name = edit_name.getText().toString();
                        String phone = edit_phone.getText().toString();
                        String address = edit_address.getText().toString();

                        String commodity = txv_commodity.getText().toString();
                        String price = txv_price.getText().toString();
                        String sale = txv_sale.getText().toString();

                        insert();

                        Intent send_check_out_info_intent = new Intent(CheckOut_Activity.this, WebviewActivity.class);

                        send_check_out_info_intent.putExtra("check_name", name);
                        send_check_out_info_intent.putExtra("check_phone", phone);
                        send_check_out_info_intent.putExtra("check_address", address);
                        send_check_out_info_intent.putExtra("check_commodity", commodity);
                        send_check_out_info_intent.putExtra("check_price", price);
                        send_check_out_info_intent.putExtra("check_sale", sale);
                        startActivity(send_check_out_info_intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.check_out_dialog_cannel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void insert() {

        try {
            String name = edit_name.getText().toString();
            String phone = edit_phone.getText().toString();
            String address = edit_address.getText().toString();
            String email = edit_email.getText().toString();

            addData(name, phone, address, email);

            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void addData(String dbname, String dbphone, String dbaddress, String dbemail) {
        ContentValues contentValues = new ContentValues(4);
        contentValues.put("name", dbname);
        contentValues.put("phone", dbphone);
        contentValues.put("address", dbaddress);
        contentValues.put("email", dbemail);

        db.insert(tb_name, null, contentValues);
    }

    private void show() {
        cursor = db.rawQuery("SELECT * FROM " + tb_name, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show();
            cursor = db.rawQuery("SELECT * FROM " + tb_name, null);
        }

        if (cursor.moveToFirst()) {
            String str = "總共" + cursor.getCount() + "筆\n";

            do {
                edit_name.setText(cursor.getString(0));
                edit_phone.setText(cursor.getString(1));
                edit_address.setText(cursor.getString(2));
                edit_email.setText(cursor.getString(3));
            } while (cursor.moveToNext());

        }
    }

    public void user_db_show(View view) {
        show();
    }
}
