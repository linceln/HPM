package com.olsplus.balancemall.app.merchant.goods.viewholder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.goods.bean.GoodsListEntity;
import com.olsplus.balancemall.component.image.ImageHelper;
import com.olsplus.balancemall.core.util.UIUtil;

public class GoodsViewHolder extends BaseViewHolder<GoodsListEntity.ProductListBean> {

    private final ImageView ivItemGoodsImg;
    private final TextView tvItemGoodsTitle;
    private final TextView tvItemGoodsPrice;
    private final TextView tvItemGoodsReserve;
    private final TextView tvItemGoodsSale;

    public GoodsViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        ivItemGoodsImg = $(R.id.ivItemGoodsImg);
        tvItemGoodsTitle = $(R.id.tvItemGoodsTitle);
        tvItemGoodsPrice = $(R.id.tvItemGoodsPrice);
        tvItemGoodsReserve = $(R.id.tvItemGoodsReserve);
        tvItemGoodsSale = $(R.id.tvItemGoodsSale);
    }

    @Override
    public void setData(GoodsListEntity.ProductListBean data) {
        super.setData(data);

        ImageHelper.display(getContext(), ivItemGoodsImg, data.getThumbnail());
        tvItemGoodsTitle.setText(data.getTitle());
        tvItemGoodsPrice.setText(UIUtil.formatLablePrice(data.getPrice()));
        tvItemGoodsReserve.setText("库存：" + data.getInventory());
        tvItemGoodsSale.setText("销量：" + data.getSold());

    }
}
