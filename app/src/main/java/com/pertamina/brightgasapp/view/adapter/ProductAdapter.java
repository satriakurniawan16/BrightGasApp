package com.pertamina.brightgasapp.view.adapter;

import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.pertamina.brightgasapp.R;
import com.pertamina.brightgasapp.model.Order.Order;
import com.pertamina.brightgasapp.model.Order.OrderProductData;
import com.pertamina.brightgasapp.model.Order.OrderProductResult;
import com.pertamina.brightgasapp.model.login.LoginModel;
import com.pertamina.brightgasapp.model.productList.ProductData;
import com.pertamina.brightgasapp.model.productList.ProductResult;
import com.pertamina.brightgasapp.view.utilities.Preference;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductResult> movies;
    private List<OrderProductResult> order = new ArrayList<>();
    private OrderProductData productData = new OrderProductData();
    private View emptyView;
    int limit = 0 ;


    public ProductAdapter(List<ProductResult> movies, View emptyView) {
        this.movies = movies;
        this.emptyView = emptyView;
    }

    public void updateEmptyView() {
        if (movies.size() == 0 || movies == null)
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, viewGroup, false);
        return new ProductViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int position) {
        productViewHolder.bind(movies.get(position), position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productTitle;
        TextView productPrice;
        TextView productDesc;
        TextView productQty;
        LinearLayout add;
        LinearLayout dec;

        ProductViewHolder(@NonNull View itemView) {

            super(itemView);
            productImage = itemView.findViewById(R.id.image_product);
            productTitle = itemView.findViewById(R.id.title_product);
            productDesc = itemView.findViewById(R.id.desc_product);
            productPrice = itemView.findViewById(R.id.price_product);
            productQty = itemView.findViewById(R.id.qty);
            add = itemView.findViewById(R.id.inc_qty);
            dec = itemView.findViewById(R.id.dec_qty);

        }

        void bind(ProductResult product, int position) {

            final int[] i = {0};
            Preference preference = new Preference(itemView.getContext());

            Glide.with(itemView.getContext())
                    .load("https://api.brightgas.co.id/" + product.getImage())
                    .into(productImage);

            productTitle.setText(product.getName());
            productDesc.setText(product.getDescription());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                productDesc.setText(Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                productDesc.setText(Html.fromHtml(product.getDescription()));
            }
            productPrice.setText(product.getPrice());

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(limit<3){
                        i[0]++;
                        limit++;
                        int status = 0;
                        productQty.setText(String.valueOf(i[0]));
                        OrderProductResult orderProductResult = new OrderProductResult();

                        orderProductResult.setName(product.getName());
                        orderProductResult.setProduct_id(product.getId());
                        orderProductResult.setQty(String.valueOf(i[0]));
                        orderProductResult.setPrice(product.getPrice());

                        OrderProductResult orderProductResult1 = new OrderProductResult();

//                    productData.add(orderProductResult);
                        if (order.size() == 0) {
                            order.add(orderProductResult);
                        } else {

                            for (int j = 0; j < order.size(); j++) {
                                if (product.getId().equals(order.get(j).getProduct_id())) {
                                    order.set(j, orderProductResult);
                                    status = 1;
                                    break;
                                }
                            }

                            if (status == 0) {
                                order.add(orderProductResult);
                            }
                            Log.d("sabana", "onClick: " + order.toString());
                        }

                        productData.setProductResults(order);
                        Gson gson = new Gson();
                        String jsonNew = gson.toJson(productData);

                        preference.setProduct(jsonNew);
                    }else {
                        Toast.makeText(itemView.getContext(), "Sudah mencapai batas maksimal", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status = 0;
                    if (i[0] > 0) {
                        i[0]--;
                        limit--;
                        productQty.setText(String.valueOf(i[0]));
                        OrderProductResult orderProductResult = new OrderProductResult();

                        orderProductResult.setName(product.getName());
                        orderProductResult.setProduct_id(product.getId());
                        orderProductResult.setQty(String.valueOf(i[0]));
                        orderProductResult.setPrice(product.getPrice());

                        for (int j = 0; j < order.size(); j++) {
                            if (product.getId().equals(order.get(j).getProduct_id())) {
                                if (i[0] == 0) {
                                    order.remove(j);
                                    break;
                                } else {
                                    order.set(j, orderProductResult);
                                }
                                break;
                            }
                        }

                        Gson gson = new Gson();
                        productData.setProductResults(order);
                        String json = gson.toJson(productData);
                        preference.setProduct(json);
                       }
                }
            });

        }
    }

}