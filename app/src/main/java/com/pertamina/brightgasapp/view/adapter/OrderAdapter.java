package com.pertamina.brightgasapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.model.Order.OrderProductResult;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter .OrderViewHolder> {
    private List<OrderProductResult> movies;

    public OrderAdapter(List<OrderProductResult> movies) {
        this.movies = movies;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_order, viewGroup, false);
        return new OrderViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int position) {
        orderViewHolder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView productTitle;
        TextView productPrice;
        TextView productQty;

        OrderViewHolder(@NonNull View itemView) {

            super(itemView);
            productTitle = itemView.findViewById(R.id.name_product);
            productPrice = itemView.findViewById(R.id.price_product);
            productQty = itemView.findViewById(R.id.qty_product);

        }

        void bind(OrderProductResult product) {

            productTitle.setText(product.getName());
            productPrice.setText("Rp."+product.getPrice() );
            productQty.setText(product.getQty()+ " tabung");

            }

        }
    }

