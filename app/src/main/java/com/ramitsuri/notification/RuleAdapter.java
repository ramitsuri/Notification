package com.ramitsuri.notification;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 310247189 on 8/8/2016.
 */
public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.CustomViewHolder>  {

    private ArrayList<NotificationRule> rules;
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView packageName;
        protected TextView filterText;
        protected TextView notificationTitle;
        protected TextView notificationText;
        protected TextView openOriginalApp;
        protected TextView enabled;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.packageName = (TextView) itemView.findViewById(R.id.rulePackageName);
            this.filterText = (TextView) itemView.findViewById(R.id.ruleFilterText);
            this.notificationTitle = (TextView) itemView.findViewById(R.id.ruleNotificationTitle);
            this.notificationText = (TextView) itemView.findViewById(R.id.ruleNotificationText);
            this.openOriginalApp = (TextView) itemView.findViewById(R.id.ruleNotificationOpenOriginal);
            this.enabled = (TextView) itemView.findViewById(R.id.ruleEnabled);
        }
    }

    public RuleAdapter(ArrayList<NotificationRule> myDataset) {
        rules = myDataset;
    }

    @Override
    public RuleAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.packageName.setText(rules.get(position).getPackageName());
        holder.filterText.setText(rules.get(position).getFilterText());
        holder.notificationTitle.setText(rules.get(position).getNewNotification().getTitle());
        holder.notificationText.setText(rules.get(position).getNewNotification().getText());
        holder.openOriginalApp.setText(String.valueOf(rules.get(position).getNewNotification().getOpenOriginalApp()));
        holder.enabled.setText(String.valueOf(rules.get(position).getIsEnabled()));

    }

    @Override
    public int getItemCount() {
        return rules.size();
    }
}
