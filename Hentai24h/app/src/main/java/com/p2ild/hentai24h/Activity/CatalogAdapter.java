package com.p2ild.hentai24h.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.p2ild.hentai24h.R;

import java.util.ArrayList;

/**
 * Created by duypi on 8/30/2016.
 */
public class CatalogAdapter extends BaseAdapter{
    private ArrayList<Catalog> arrCtl;
    private MyHolder mHolder;

    public CatalogAdapter(ArrayList<Catalog> arrCtl) {
        this.arrCtl = arrCtl;
    }

    @Override
    public int getCount() {
        return arrCtl.size();
    }

    @Override
    public Object getItem(int i) {
        return arrCtl.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lst,viewGroup,false);
            mHolder = new MyHolder();
            mHolder.catalogName = (TextView)view.findViewById(R.id.catalog_name);
            view.setTag(mHolder);
        }else {
            mHolder = (MyHolder) view.getTag();
        }

        mHolder.catalogName.setText(arrCtl.get(i).getNameCatalog());
        return view;
    }

    private class MyHolder {
        private TextView catalogName;
    }
}
