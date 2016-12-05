package com.olsplus.balancemall.app.merchant.goods.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.goods.bean.GoodsDetail;
import com.olsplus.balancemall.component.recycler.BaseAdapter;
import com.olsplus.balancemall.component.recycler.CustomViewHolder;

import java.util.List;

import static com.olsplus.balancemall.core.util.StrConst.input.adding;
import static com.olsplus.balancemall.core.util.StrConst.input.editing;

public class AddGoodsAdapter extends BaseAdapter<GoodsDetail.SkuInfo> {

    private int input = adding;

    public AddGoodsAdapter(Context context, List<GoodsDetail.SkuInfo> list) {
        super(context, list);
    }

    public void setInput(int input) {
        this.input = input;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_add_spec, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        GoodsDetail.SkuInfo bean = list.get(position);

        if (position == 0) {
            holder.setVisibility(R.id.ivDeleteItem, View.GONE);
            holder.setVisibility(R.id.linearItemSpec, View.GONE);
        }

        holder.setOnClickListener(R.id.ivDeleteItem, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        if (input == editing) {
            if (bean.getPrice() != 0) {
                holder.setText(R.id.etItemPrice, String.valueOf(bean.getPrice()));
            }
            if (bean.getInventory() != 0) {
                holder.setText(R.id.etItemReserve, String.valueOf(bean.getInventory()));
            }
            if (TextUtils.isEmpty(bean.getProperty()) && position == 0) {
                holder.setVisibility(R.id.linearItemSpec, View.GONE);
            } else {
                holder.setVisibility(R.id.linearItemSpec, View.VISIBLE);
                holder.setText(R.id.etItemSpec, bean.getProperty());
            }
        }

        holder.setTextWatcher(R.id.etItemSpec, new NewTextWatcher(holder, bean));
        holder.setTextWatcher(R.id.etItemPrice, new NewTextWatcher(holder, bean));
        holder.setTextWatcher(R.id.etItemReserve, new NewTextWatcher(holder, bean));
    }

    private class NewTextWatcher implements TextWatcher {


        private final CustomViewHolder holder;
        private final GoodsDetail.SkuInfo bean;

        public NewTextWatcher(CustomViewHolder holder, GoodsDetail.SkuInfo bean) {

            this.holder = holder;
            this.bean = bean;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String spec = holder.getText(R.id.etItemSpec);
            String price = holder.getText(R.id.etItemPrice);
            String reserve = holder.getText(R.id.etItemReserve);

            if (!TextUtils.isEmpty(price)) {
                bean.setPrice(Double.parseDouble(price));
            }

            if (!TextUtils.isEmpty(reserve)) {
                bean.setInventory(Integer.parseInt(reserve));
            }

            if (!TextUtils.isEmpty(spec)) {
                bean.setProperty(spec);
            } else {
                bean.setProperty("");
            }
        }
    }
}
