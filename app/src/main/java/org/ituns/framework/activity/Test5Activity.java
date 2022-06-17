package org.ituns.framework.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.ituns.framework.R;

public class Test5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test5);
    }

    public static void load(Activity activity) {
        Intent intent = new Intent(activity, Test5Activity.class);
        activity.startActivity(intent);
    }
}