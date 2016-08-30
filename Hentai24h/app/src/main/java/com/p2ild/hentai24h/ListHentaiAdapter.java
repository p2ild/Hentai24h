package com.p2ild.hentai24h;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by duypi on 8/28/2016.
 */
public class ListHentaiAdapter extends RecyclerView.Adapter<ListHentaiAdapter.MyHolder> {
	private static final String TAG = ListHentaiAdapter.class.getSimpleName();
	private final Context context;
	private final ArrayList<Hentai> arrSrc;

	public ListHentaiAdapter(Context context, ArrayList<Hentai> arrSrc) {
		this.context = context;
		this.arrSrc = arrSrc;
	}

	@Override
	public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv,parent,false);
		MyHolder myHolder = new MyHolder(view);
		return myHolder;
	}

	@Override
	public void onBindViewHolder(MyHolder myHolder, int position) {
		myHolder.tenTruyen.setText(arrSrc.get(position).getTenTruyen()+"");
		Glide
				.with(context)
				.load(arrSrc.get(position).getLinkImgPreview())
//				.centerCrop()
				.placeholder(R.drawable.placeholder_blue)
				.crossFade()
				.into(myHolder.img);
	}

	@Override
	public int getItemCount() {
		return arrSrc.size();
	}

	public class MyHolder extends RecyclerView.ViewHolder {
		private TextView tenTruyen;
		private ImageView img;

		public MyHolder(View itemView) {
			super(itemView);
			tenTruyen = (TextView)itemView.findViewById(R.id.ten_truyen);
			img = (ImageView) itemView.findViewById(R.id.img);
		}
	}
}
