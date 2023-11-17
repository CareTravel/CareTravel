package com.example.caretravel;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class nameHolder extends RecyclerView.ViewHolder {
    private final TextView mNameField;

    public nameHolder(@NonNull View itemView) {
        super(itemView);
        mNameField = itemView.findViewById(android.R.id.text1);
    }

    public void bind(@NonNull RoomData roomData) {
        setName(roomData.getName());
    }

    private void setName(@Nullable String name) {
        mNameField.setText(name);
    }
}
