package com.thematic.end_report;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class BranchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
    }

    public void branch_map(View view) {
        String location = "";
        switch (view.getId()) {
            case R.id.google_map:
                location = "1600 Amphitheatre Parkway, Mountain+View, California";
                break;
            case R.id.lhu_map:
                location = "桃園市龜山區萬壽路一段300號";
                break;
        }
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
