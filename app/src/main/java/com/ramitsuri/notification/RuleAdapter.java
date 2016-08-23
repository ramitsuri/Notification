package com.ramitsuri.notification;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ramitsuri.notification.db.SQLHelper;

import java.util.ArrayList;

/**
 * Created by 310247189 on 8/8/2016.
 */
public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.CustomViewHolder>  {

    private ArrayList<NotificationRule> rules;
    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView appName;
        protected TextView filterText;
        protected TextView notificationTitle;
        protected TextView notificationText;
        protected TextView openOriginalApp;
        protected SwitchCompat enabled;
        private SQLHelper sqlHelper;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.sqlHelper = SQLHelper.getInstance(itemView.getContext());
            this.appName = (TextView) itemView.findViewById(R.id.ruleAppName);
            //this.filterText = (TextView) itemView.findViewById(R.id.ruleFilterText);
            this.notificationTitle = (TextView) itemView.findViewById(R.id.ruleNotificationTitle);
            this.notificationText = (TextView) itemView.findViewById(R.id.ruleNotificationText);
            //this.openOriginalApp = (TextView) itemView.findViewById(R.id.ruleNotificationOpenOriginal);
            this.enabled = (SwitchCompat) itemView.findViewById(R.id.ruleEnabledToggle);
            this.enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    NotificationRule rule = rules.get(getAdapterPosition());
                    rule.setEnabled(b);
                    sqlHelper.editRule(rule);
                    NotificationListener.refreshRules(compoundButton.getContext());
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            NotificationRule rule = rules.get(getAdapterPosition());
            Intent addRuleActivityIntent = new Intent(view.getContext(), AddRuleActivity.class);
            addRuleActivityIntent.setAction(AddRuleActivity.ACTION_EDIT);
            addRuleActivityIntent.putExtra(AddRuleActivity.RULE_TO_EDIT, rule);
            view.getContext().startActivity(addRuleActivityIntent);
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
        holder.appName.setText(rules.get(position).getAppName());
        //holder.filterText.setText(rules.get(position).getFilterText());
        holder.notificationTitle.setText(rules.get(position).getNewNotification().getTitle());
        holder.notificationText.setText(rules.get(position).getNewNotification().getText());
        //holder.openOriginalApp.setText(String.valueOf(rules.get(position).getNewNotification().getOpenOriginalApp()));
        holder.enabled.setChecked(rules.get(position).getIsEnabled());

    }

    @Override
    public int getItemCount() {
        return rules.size();
    }

    public void updateDataSet(ArrayList<NotificationRule> rules){
        this.rules = rules;
        this.notifyDataSetChanged();
    }
}
