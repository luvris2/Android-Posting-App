package com.luvris2.postingapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.luvris2.postingapp.MainActivity;
import com.luvris2.postingapp.R;
import com.luvris2.postingapp.model.Posting;
import com.luvris2.postingapp.ui.AddActivity;
import com.luvris2.postingapp.ui.EditActivity;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    ArrayList<Posting> postingList;

    public Adapter(Context context, ArrayList<Posting> postingList) {
        this.context = context;
        this.postingList = postingList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posting_row, parent, false);
        return new Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Posting Posting = postingList.get(position);
        holder.txtTitle.setText(Posting.title);
        holder.txtContent.setText(Posting.content);
    }

    @Override
    public int getItemCount() {
        return postingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtContent;
        ImageView imgDelete;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            imgDelete.setOnClickListener(view -> {
                int index = getAdapterPosition();

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("포스팅 삭제");
                alert.setMessage("정말 삭제하시겠습니까?");
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postingList.remove(index);
                        notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("아니오", null);
                alert.show();
            });

            cardView.setOnClickListener(view -> {
                int index = getAdapterPosition();
                Intent intent = new Intent(context, EditActivity.class);

                Posting Posting = postingList.get(index);
                intent.putExtra("Posting", Posting);
                intent.putExtra("index", index);
                ((MainActivity)context).startActivityResult.launch(intent);
            });
        }
    }
}