package com.cbstudio.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cbstudio.colapicker.activity.ColaActivity;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {

    private final int REQUEST_PICK_IMAGE = 2;

    private LinearLayout mRootImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootImage = (LinearLayout)findViewById(R.id.root_images);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private  void updateImages(Uri[] uris)
    {
        mRootImage.removeAllViews();

        for(int i = 0 ; i < uris.length ; ++i){
            Uri uri = uris[i];

            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            Glide.with(this).load(new File(uri.getPath())).fitCenter().crossFade().into(iv);

            mRootImage.addView(iv);
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_select){
            Intent i = new Intent(this, ColaActivity.class);
            i.putExtra(ColaActivity.EXTRA_LIMIT_COUNT, 10);

            startActivityForResult(i, REQUEST_PICK_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_PICK_IMAGE){
            if(resultCode == RESULT_OK) {
                Parcelable[] parcelableUris = data.getParcelableArrayExtra(ColaActivity.EXTRA_IMAGE_URIS);
                if (parcelableUris != null) {
                    Uri[] uris = new Uri[parcelableUris.length];
                    System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);

                    updateImages(uris);
                }
            }
        }
    }
}
