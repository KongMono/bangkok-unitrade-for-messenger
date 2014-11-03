package com.codefriday.bangkokunitrade.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.codefriday.bangkokunitrade.R;
import com.codefriday.bangkokunitrade.dataset.DetailEntry;

public class ListDetailAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<DetailEntry> items = new ArrayList<DetailEntry>();
	protected Context context;
	private String TAG = getClass().getName();
	ViewHolder holder;
	AQuery aQuery;

	public class ViewHolder {
		public TextView no;
		public TextView sale;
		public TextView hospital;
		public int position;
	}

	public ListDetailAdapter(Context context, ArrayList<DetailEntry> items) {
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
		DetailEntry item = items.get(position);
		View view = convertView;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.item_list_detail, parent, false);
			holder = new ViewHolder();
			holder.no = (TextView)view.findViewById(R.id.no);
			holder.sale = (TextView)view.findViewById(R.id.sale);
			holder.hospital = (TextView)view.findViewById(R.id.hospital);
			holder.position = position;
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.no.setText(": " + item.getNo());
		holder.no.setTextSize(15f);			

		holder.sale.setText("item: " + item.getSale());
		holder.sale.setTextSize(15f);		
		
		holder.hospital.setText("description: " + item.getHospital());
		holder.hospital.setTextSize(15f);	
		
		return view;
	}
}
