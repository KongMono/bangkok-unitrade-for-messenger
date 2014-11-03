package com.codefriday.bangkokunitrade.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.codefriday.bangkokunitrade.R;
import com.codefriday.bangkokunitrade.dataset.StoreEntry;

public class ListStoreAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<StoreEntry> items = new ArrayList<StoreEntry>();
	protected Context context;
	private String TAG = getClass().getName();
	ViewHolder holder;
	AQuery aQuery;

	public class ViewHolder {
		public TextView request_no;
		public ImageView status;
		public int position;
	}

	public ListStoreAdapter(Context context, ArrayList<StoreEntry> items) {
		this.mInflater = LayoutInflater.from(context);
		this.items = items;
		this.context = context;
		aQuery = new AQuery(context);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int pos) {
		return items.get(pos);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StoreEntry item = items.get(position);
		View view = convertView;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.item_list_store, parent, false);
			holder = new ViewHolder();
			holder.request_no = (TextView)view.findViewById(R.id.request_no);
			holder.status = (ImageView)view.findViewById(R.id.status);
			holder.position = position;
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.request_no.setText(item.getRequest_no());
//		holder.implantName.setTypeface(Util.getBoldFont(context));
		holder.request_no.setTextSize(30f);			

		if (item.getStatus().equalsIgnoreCase("1")) {
			holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.sending));
		}else{
			holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.waiting_sending));
		}
		
		return view;
	}
}
