package org.ituns.framework.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.ituns.framework.R;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Test6Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test6);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        List<String> list = Arrays.asList("1", "2", "3", "4", "5");
    }

    public static void load(Activity activity) {
        Intent intent = new Intent(activity, Test6Activity.class);
        activity.startActivity(intent);
    }
}