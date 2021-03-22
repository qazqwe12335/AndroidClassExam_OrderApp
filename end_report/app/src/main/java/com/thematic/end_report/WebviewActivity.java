package com.thematic.end_report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
    }

    private void init() {

        String name = getIntent().getStringExtra("check_name");
        String phone = getIntent().getStringExtra("check_phone");
        String address = getIntent().getStringExtra("check_address");
        String food = getIntent().getStringExtra("check_commodity");
        String oprice = getIntent().getStringExtra("check_price");
        String sprice = getIntent().getStringExtra("check_sale");

        WebView webv = findViewById(R.id.webview_page);
        webv.loadUrl("http://forum.twbts.com/sans.php?name=" + name + "&address=" + address + "&tel=" + phone + "&foods=" + food + "&oprice=" + oprice + "&sprice=" + sprice);
    }
}
