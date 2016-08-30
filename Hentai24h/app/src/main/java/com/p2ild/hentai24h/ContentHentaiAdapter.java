package com.p2ild.hentai24h;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by duypi on 8/28/2016.
 */
public class ContentHentaiAdapter extends RecyclerView.Adapter<ContentHentaiAdapter.MyHolder> {
	private static final String TAG = ContentHentaiAdapter.class.getSimpleName();
	private final Context context;
	private final ArrayList<String> arrSrc;

	public ContentHentaiAdapter(Context context, ArrayList<String> arrSrc) {
		this.context = context;
		this.arrSrc = arrSrc;
	}

	@Override
	public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
		MyHolder myHolder = new MyHolder(view);
		return myHolder;
	}

	@Override
	public void onBindViewHolder(MyHolder myHolder, int position) {
		Log.d(TAG, "onBindViewHolder: arrSrc.get(position): "+arrSrc.get(position));
		Glide
				.with(context)
				.load(arrSrc.get(position))
//				.centerCrop()
				.placeholder(R.drawable.placeholder_blue)
				.crossFade()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(myHolder.img);
	}

	@Override
	public int getItemCount() {
		return arrSrc.size();
	}

	public class MyHolder extends RecyclerView.ViewHolder {
		private ImageView img;

		public MyHolder(View itemView) {
			super(itemView);
			img = (ImageView) itemView.findViewById(R.id.img);
		}
	}
}
