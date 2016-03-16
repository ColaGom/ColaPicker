package com.cbstudio.colapicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cbstudio.colapicker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colabear on 2016-03-16.
 */
public class SimpleGridAdapter extends ArrayAdapter<Uri> {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Uri> mSelectedUri;

    public SimpleGridAdapter(Context context, int resource, List<Uri> objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mSelectedUri = new ArrayList<Uri>();
    }

    public List<Uri> getSelectedUri()
    {
        return mSelectedUri;
    }

    public void select(Uri uri, View view)
    {
        ViewHolder holder = (ViewHolder)view.getTag();

        if(containsUri(uri)){
            mSelectedUri.remove(uri);
            holder.rootCurver.setVisibility(View.GONE);
        }else{
            mSelectedUri.add(uri);
            holder.rootCurver.setVisibility(View.VISIBLE);
            holder.tvIndex.setText((getIndex(uri) + 1) + "");
        }
    }

    public boolean containsUri(Uri uri)
    {
        return mSelectedUri.contains(uri);
    }

    private int getIndex(Uri image)
    {
        for(int i = 0 ; i < mSelectedUri.size() ; ++i)
            if(mSelectedUri.get(i).equals(image)) return i;
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.row_grid, parent, false);
            holder = new ViewHolder(convertView);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bind(getItem(position));

        return convertView;
    }

    class ViewHolder
    {
        ImageView ivImage;
        TextView tvIndex;
        View rootCurver;

        ViewHolder(View view)
        {
            ivImage = (ImageView) view.findViewById(R.id.iv_image);
            tvIndex = (TextView) view.findViewById(R.id.tv_index);
            rootCurver = view.findViewById(R.id.root_curver);
            view.setTag(this);
        }

        void bind(Uri uri)
        {
            if(containsUri(uri)){
                rootCurver.setVisibility(View.VISIBLE);
                tvIndex.setText((getIndex(uri) + 1) + "");
            }else{
                rootCurver.setVisibility(View.GONE);
            }

            Glide.with(mContext).load(new File(uri.getPath())).centerCrop().crossFade().into(ivImage);
        }
    }
}
