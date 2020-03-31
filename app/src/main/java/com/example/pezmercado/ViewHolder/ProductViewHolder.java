package com.example.pezmercado.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pezmercado.Interface.ItemClickListner;
import com.example.pezmercado.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtProductName, txtProductDescription, txtProductPrice;
        public ImageView imageView;
        public ItemClickListner listner;

        public ProductViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.select_product_image);
            txtProductName = (TextView) itemView.findViewById(R.id.product_name);
            txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
            txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);

        }

        public void setItemClickListner(ItemClickListner listner){
            this.listner = listner;

        }

        @Override
        public void onClick(View view) {

            listner.onClick(view, getAdapterPosition(), false);
        }
    }

