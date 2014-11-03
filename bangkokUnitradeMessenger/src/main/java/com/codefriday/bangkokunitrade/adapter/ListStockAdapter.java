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
import com.codefriday.bangkokunitrade.dataset.StockEntry;

public class ListStockAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<StockEntry> items = new ArrayList<StockEntry>();
	protected Context context;
	private String TAG = getClass().getName();
	ViewHolder holder;
	AQuery aQuery;

	public class ViewHolder {
		public TextView implantName;
		public TextView Hospital;
		public ImageView status;
		public int position;
	}

	public ListStockAdapter(Context context, ArrayList<StockEntry> items) {
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
		StockEntry item = items.get(position);
		View view = convertView;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.item_stock, parent, false);
			holder = new ViewHolder();
			holder.implantName = (TextView)view.findViewById(R.id.implantName);
			holder.Hospital = (TextView)view.findViewById(R.id.Hospital);
			holder.status = (ImageView)view.findViewById(R.id.status);
			holder.position = position;
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.implantName.setText("เลขที่ส่งของ: " + item.getNo());
//		holder.implantName.setTypeface(Util.getBoldFont(context));
		holder.implantName.setTextSize(15f);			

		holder.Hospital.setText(item.getHospital());
//		holder.Hospital.setTypeface(Util.getBoldFont(context));
		holder.Hospital.setTextSize(15f);		
		
		if (item.getStatus().equalsIgnoreCase("1")) {
			holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.sending));
		}else{
			holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.waiting_sending));
		}
		
		return view;
	}
}
