package com.thematic.end_report;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String food[];
    String price[];

    ArrayList<String> posi;
    ArrayList<String> posi_price;

    Resources res;

    ListView listview;

    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        res = getResources();
        food = res.getStringArray(R.array.food_name);
        price = res.getStringArray(R.array.money);

        listview = findViewById(R.id.listview);

        myAdapter = new MyAdapter(this, food, price);
        listview.setAdapter(myAdapter);

        posi = new ArrayList();
        posi_price = new ArrayList<>();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!posi.contains(food[i])) {
                    posi.add(food[i]);
                    posi_price.add(price[i]);
                    view.setBackgroundColor(res.getColor(R.color.gray_color));
                } else {
                    posi.remove(food[i]);
                    posi_price.remove(price[i]);
                    view.setBackgroundColor(res.getColor(R.color.white_color));
                }
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("促銷通知")
                .setMessage("凡購買任意商品滿500 立刻打9折")
                .setNegativeButton("了解", null)
                .show();
    }

    public void complaint(View view) {
        Intent complaint_intent = new Intent(this, Costomer_Complaint_Email_Activity.class);
        startActivity(complaint_intent);
    }

    public void branch_page(View view) {
        Intent branch_page_intent = new Intent(this, BranchActivity.class);
        startActivity(branch_page_intent);
    }

    public void checkout(View view) {
        String str = "";
        int i = 0;
        Intent checkout_intent = new Intent(this, CheckOut_Activity.class);
        for (int ShipNum = 0; ShipNum < posi.size(); ShipNum++) {
            str += posi.get(ShipNum) + "  ";
            i += Integer.valueOf(posi_price.get(ShipNum));
        }

        checkout_intent.putExtra("food_name", str);
        checkout_intent.putExtra("price", String.valueOf(i));

        startActivity(checkout_intent);
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String[] food;
        String[] price;

        public MyAdapter(Context context, String[] food_name, String[] food_price) {
            super(context, R.layout.recyclerview_view, R.id.list_food, food_name);

            this.context = context;
            this.food = food_name;
            this.price = food_price;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.recyclerview_view, parent, false);
            TextView food_txv = row.findViewById(R.id.list_food);
            TextView price_txv = row.findViewById(R.id.list_price);

            food_txv.setText(food[position]);
            price_txv.setText(price[position]);

            return row;
        }
    }
}
