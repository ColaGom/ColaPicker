package com.cbstudio.colapicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cbstudio.colapicker.R;
import com.cbstudio.colapicker.adapter.SimpleGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class ColaActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String EXTRA_LIMIT_COUNT = "extra.limit.count";
    public static final String EXTRA_IMAGE_URIS = "extra.image.uris";

    private final int DEFAULT_LENGTH = 5;

    private GridView mGvMain;
    private SimpleGridAdapter mAdapter;
    private View mBtnComplete;
    private View mBtnBack;
    private int mLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cola);

        Bundle b = getIntent().getExtras();

        if(b != null)
            mLimit = b.getInt(EXTRA_LIMIT_COUNT, DEFAULT_LENGTH);
        else
            mLimit = DEFAULT_LENGTH;

        mGvMain = (GridView) findViewById(R.id.gv_main);
        mBtnComplete = findViewById(R.id.btn_complete);
        mBtnBack = findViewById(R.id.btn_back);

        loadImage();
        notifyComponents();
    }

    private void loadImage() {
        List<Uri> uriList = new ArrayList<>();
        Cursor imageCursor = null;

        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                    orderBy);
            while (imageCursor.moveToNext()) {
                Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                uriList.add(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }

        mAdapter = new SimpleGridAdapter(this, 0, uriList);
        mGvMain.setAdapter(mAdapter);
        mGvMain.smoothScrollByOffset(0);
        mGvMain.setOnItemClickListener(this);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cola, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id)
        {
            case R.id.btn_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btn_complete:
                Uri[] arrUri = mAdapter.getSelectedUri().toArray(new Uri[mAdapter.getSelectedUri().size()]);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_IMAGE_URIS, arrUri);
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Uri uri = (Uri) adapterView.getAdapter().getItem(i);

        if (mAdapter.getSelectedUri().size() == mLimit && !mAdapter.containsUri(uri)) {
            Toast.makeText(ColaActivity.this, "Already selected max length.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAdapter.select(uri, view);
        notifyComponents();
    }

    private void notifyComponents() {
        if (mAdapter.getSelectedUri().size() == 0)
            mBtnComplete.setVisibility(View.GONE);
        else
            mBtnComplete.setVisibility(View.VISIBLE);
    }
}
