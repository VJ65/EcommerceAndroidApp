package com.solodroid.ecommerce;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// adapter class for custom main menu list
class AdapterMainMenu extends BaseAdapter {
		private Activity activity;
		
		String[] Main_menu;
		
		public AdapterMainMenu(Activity act) {
			this.activity = act;
			Main_menu = act.getResources().getStringArray(R.array.main_menu_list);
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return Main_menu.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.main_menu_item, null);
				holder = new ViewHolder();
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.txtText = (TextView) convertView.findViewById(R.id.txtText);
			
			holder.txtText.setText(Main_menu[position]);
			
			return convertView;
		}
		
		static class ViewHolder {
			TextView txtText;
		}
		
	}