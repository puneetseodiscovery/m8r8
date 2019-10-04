package com.m8sworld.m8r8.myprofile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.m8sworld.m8r8.R;
import com.m8sworld.m8r8.util.Config;

import java.util.ArrayList;

public class MandateAdapter extends RecyclerView.Adapter<MandateAdapter.ViewHolder> {

    Context context;
    ArrayList<MandateListApis.Datum> arrayList = new ArrayList<>();

    GetId getId;
    GetForgive getForgive;

    public void setGetForgive(GetForgive getForgive1){
        getForgive=getForgive1;
    }


    public void setGetId(GetId getId1) {
        getId = getId1;

    }

    public void setGetHand(GetId getId1) {
        getId = getId1;
    }

    public MandateAdapter(Context context, ArrayList<MandateListApis.Datum> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_mandate, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final MandateListApis.Datum datum = arrayList.get(i);
        viewHolder.txtData.setText(datum.getCreatedAt().toString());
        viewHolder.txtName.setText(datum.getName() + "\n( " + datum.getUserType() + " )");
        viewHolder.txtStatus.setText(datum.getStatus().toString());


        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Config.PDF_URL + datum.getPdfInvoice();
                Log.d("Url123", url);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });

        viewHolder.imageDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Config.PDF_URL + datum.getPdfInvoice();
                Log.d("Url123", url);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });


        viewHolder.imageAngel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForgive.onSucess(datum.getInvoiceId());
            }
        });

        viewHolder.imageHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getId.getInvoiceId(datum.getInvoiceId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtData, txtStatus, txtName;
        ImageView imageView, imageDownload, imageAngel, imageHand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtName = itemView.findViewById(R.id.txtLender);
            imageView = itemView.findViewById(R.id.imageView);
            imageDownload = itemView.findViewById(R.id.imageDownload);
            imageAngel = itemView.findViewById(R.id.imageAngel);
            imageHand = itemView.findViewById(R.id.imageHand);
        }
    }

}
