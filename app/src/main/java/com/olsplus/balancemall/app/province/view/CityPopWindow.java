package com.olsplus.balancemall.app.province.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.province.bean.AddressEntity;
import com.olsplus.balancemall.component.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CityPopWindow extends PopupWindow {

    private Context context;
    private ListView listView;
    private CityAdapter cityAdapter;
    private List<AddressEntity>  addressEntityList = new ArrayList<AddressEntity>();

    private OnCityClickListener onCityClickListener;

    public interface OnCityClickListener {
        void onCityClick(String id);
    }


    public CityPopWindow(Context context) {
        super(context);
        this.context = context;
        creatView();
    }

    private void creatView() {
        setTouchable(true);
        setOutsideTouchable(true);
        setWindowLayoutMode(250, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.poponwindow_choose,null);
        listView = (ListView)view.findViewById(R.id.city_list);
        cityAdapter = new CityAdapter();
        setContentView(view);
        setWidth(250);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.child_picker_popup));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onCityClickListener!=null){
                    String cityId = addressEntityList.get(position).getId();
                    onCityClickListener.onCityClick(cityId);
                }
                dismiss();
            }
        });
    }

    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }

    public void show(View view){
        if(view.getRootView()!=null){
            int x =view.getRight()-view.getWidth()/2;
            showAsDropDown(view,-x,0);
        }
    }

    public void setView(List<AddressEntity>  addressEntityList){
        this.addressEntityList.clear();
        this.addressEntityList.addAll(addressEntityList);
        listView.setAdapter(cityAdapter);

    }

    public class CityAdapter extends BaseAdapter {

        public CityAdapter() {

        }


        @Override
        public int getCount() {
            return addressEntityList.size();
        }

        @Override
        public Object getItem(int position) {
            if (addressEntityList.isEmpty()) {
                return null;
            }
            return addressEntityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.pop_window_city_list_item, null);
            }
            TextView cityTv = ViewHolder.get(convertView,
                   R.id.text1);
            AddressEntity addressEntity= addressEntityList.get(position);
            if(addressEntity!=null){
                cityTv.setText(addressEntity.getName());
            }

            return convertView;
        }
    }

}
