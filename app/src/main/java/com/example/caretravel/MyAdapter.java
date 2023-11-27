package com.example.caretravel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.buttonText);
            Log.d("phj", "뷰 홀더 들어옴");
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String data = "";
//                    int position = getAbsoluteAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        Log.d("phj", "버튼 클릭됨");
//                        if (itemClickListener != null)
//                            itemClickListener.onItemClicked(position, data);
//                    }
//                }
//            });
        }
    }

    private ArrayList<String> list;

    MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_button, parent, false);

        return new MyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        String text = list.get(position);
        //String roomName = getIntent().getStringExtra("documentName");
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
