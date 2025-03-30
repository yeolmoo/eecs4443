package com.example.bravoproject;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bravoproject.MenuTestActivity;
import com.example.bravoproject.R;
import com.example.bravoproject.MenuItem;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<MenuItem> menuList;
    private String participantId;
    private String condition;

    public MenuAdapter(Context context, List<MenuItem> menuList, String participantId, String condition) {
        this.context = context;
        this.menuList = menuList;
        this.participantId = participantId;
        this.condition = condition;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuList.get(position);
        holder.textMenuName.setText(item.getMenuName());

        holder.itemView.setOnClickListener(v -> {
            try {
                Class<?> activityClass = Class.forName("com.example.bravoproject." + item.getActivityClassName());
                Intent intent = new Intent(context, activityClass);
                intent.putExtra("participant_id", participantId);
                intent.putExtra("condition", condition);
                intent.putExtra("menu_type", item.getMenuName());
                intent.putExtra("start_time", SystemClock.elapsedRealtime());

                ((MenuTestActivity) context).startActivityForResult(intent, 100);

            } catch (ClassNotFoundException e) {
                Toast.makeText(context, "Activity not found: " + item.getActivityClassName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView textMenuName;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            textMenuName = itemView.findViewById(R.id.textMenuName);
        }
    }
}
