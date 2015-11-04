package com.techyos.oneclickonemail.ui.activities;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.techyos.oneclickonemail.R;
import com.techyos.oneclickonemail.adapters.WidgetListAdapter;
import com.techyos.oneclickonemail.db.model.WidgetEntry;

import java.util.ArrayList;

public class MainActivity extends ActivityBase {

    /**
     * Static
     */

    private static final int WIDGET_CONFIG_CODE = 1234;

    /**
     * Attributes
     */

    private WidgetListAdapter adapter;

    /**
     * LifeCycle
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting up the recycler view
        final RecyclerView widgetListView = (RecyclerView) findViewById(R.id.widgetsListView);
        widgetListView.setHasFixedSize(true);
        widgetListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WidgetListAdapter(this, new ArrayList<WidgetEntry>(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null && v.getTag() instanceof Integer) {
                    Intent intent = new Intent(MainActivity.this, WidgetConfigActivity.class);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, (Integer) v.getTag());
                    startActivityForResult(intent, WIDGET_CONFIG_CODE);
                }
            }
        });
        widgetListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshEntries();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WIDGET_CONFIG_CODE && resultCode == RESULT_OK) {
            refreshEntries();
        }
    }

    /**
     * Private Methods
     */

    private void refreshEntries() {
        adapter.replaceAllEntries(getApp().getWidgetEntryDao().findAll());
    }
}
