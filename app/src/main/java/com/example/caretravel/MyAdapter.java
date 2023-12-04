package com.example.caretravel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private OnItemClickListener mListener;
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.buttonText);
            //Log.d("phj", "뷰 홀더 들어옴");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    String data = "";
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        //Log.d("phj", "버튼 클릭됨");
                        if (mListener != null) {
                            mListener.onItemClick(view, position);
                        }
                    }
                }
            });
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
        //Log.d("phj", "뷰 홀더 들어와서 온크리에이트 홀더");
        return new MyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        String text = list.get(position);
        //Log.d("phj", "뷰 홀더 들어와서 포지션 + "+position);
        //String roomName = getIntent().getStringExtra("documentName");
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        //Log.d("phj", "뷰 홀더 들어와서 getitem + "+list.size());
        return list.size();
    }

}


