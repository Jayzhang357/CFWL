package com.wl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.wl.cfwl.R;
import com.wl.entry.JZXX;
import com.wl.entry.JZZT;

import java.util.List;


public class DataAdapter extends BaseAdapter {
	private int[] colors = new int[] { 0xff626569, 0xff4f5257 };
	SparseBooleanArray selected;
	boolean isSingle = true;
	int old = -1;
	private class ViewHolder {
	    private TextView content1;
	    private TextView content2;
	    private TextView content3;
	    private TextView content4;
	    private TextView content5;
	    private TextView content6;    
	    private TextView content7;
	    private TextView content8;
	    private TextView content9;
	    private TextView content10;
        private TextView content11;
    }
    private List<Object> list;
	
	private LayoutInflater mInflater;
	private Context mContext;

	public DataAdapter(Context context, List<Object> data) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list=data;
		mContext = context;
		selected = new SparseBooleanArray();
	}	



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_display, parent, false);
 
            viewHolder.content1 = (TextView) view.findViewById(R.id.TextView01);
            viewHolder.content2 = (TextView) view.findViewById(R.id.TextView02);
            viewHolder.content3 = (TextView) view.findViewById(R.id.TextView03);
            viewHolder.content4 = (TextView) view.findViewById(R.id.TextView04);
            viewHolder.content5 = (TextView) view.findViewById(R.id.TextView05);
            viewHolder.content6 = (TextView) view.findViewById(R.id.TextView06);
            viewHolder.content7 = (TextView) view.findViewById(R.id.TextView07);
            viewHolder.content8 = (TextView) view.findViewById(R.id.TextView08);
            viewHolder.content9 = (TextView) view.findViewById(R.id.TextView09);
            viewHolder.content10 = (TextView) view.findViewById(R.id.TextView10);
            viewHolder.content11 = (TextView) view.findViewById(R.id.TextView11);
            view.setTag(viewHolder);
        }
        else{
	    //int colorPos = position % colors.length;
	    //view.setBackgroundColor(colors[colorPos]);
	    //view.setBackgroundColor(Color.parseColor("#90ee90"));
       //convertView.setBackgroundResource(R.drawable.selector);
        
          viewHolder = (ViewHolder) view.getTag();
        }





        JZXX info = (JZXX) list.get(position);

        viewHolder.content1.setText(String.valueOf(info.id));
        viewHolder.content2.setText(info.name);

        viewHolder.content3.setText(String.valueOf(info.b));
        viewHolder.content4.setText(String.valueOf(info.l));

        viewHolder.content5.setText(String.valueOf(info.dtbtl));
        viewHolder.content6.setText(String.valueOf(info.ip));

        viewHolder.content7.setText(String.valueOf(info.port));

        viewHolder.content8.setText(String.valueOf(info.gz));
        viewHolder.content9.setText(String.valueOf(info.mm));

        viewHolder.content10.setText(String.valueOf(info.dqy)+String.valueOf(info.qy));
        viewHolder.content11.setText(String.valueOf(info.user));
    //    viewHolder.content10.setText(String.valueOf(info.Lon_B));

		if(selected.get(position)){
			//view.setBackgroundResource(Color.BLUE);
			view.setBackgroundColor(Color.parseColor("#9999cc")); 
	
		}else{
			//view.setBackgroundResource(Color.WHITE);
			view.setBackgroundColor(Color.TRANSPARENT);  
	
		}
       /* if (position == selectItem) {  
        	view.setBackgroundColor(Color.RED);  
        }   
        else {  
        	view.setBackgroundColor(Color.TRANSPARENT);  
        }   */
        
        return view;
    }
    
	public void setSelectedItem(int selected){
		if(isSingle = true && old != -1){
			this.selected.put(old, false);
		}
		this.selected.put(selected, true);
		old = selected;
	}
    
   /* public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
   }  
   private int  selectItem=-1;  */

}






