package com.example.stepcompass.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stepcompass.Controller;
import com.example.stepcompass.Entities.UserStepData;
import com.example.stepcompass.R;
import com.example.stepcompass.Util.DateConverter;

import java.util.ArrayList;
import java.util.List;

public class StepEntriesAdapter extends RecyclerView.Adapter<StepEntriesAdapter.Holder>{
    private LayoutInflater inflater;
    private List<UserStepData> content;
    private Controller controller;

    public StepEntriesAdapter(Context context){
        this(context, new ArrayList<UserStepData>());
    }

    public StepEntriesAdapter(Context context, List<UserStepData> content){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.content = content;
    }

    public void setContent(List<UserStepData> content){
        this.content = content;
        super.notifyDataSetChanged();
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.fragment_individual_entry, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tvDate.setText(content.get(position).getDate());
        holder.tvSteps.setText(String.valueOf(content.get(position).getSteps()));
    }

    @Override
    public int getItemCount() {
        return content.size();
    }


    public class Holder extends RecyclerView.ViewHolder  {
        private TextView tvDate;
        private TextView tvSteps;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_individual_date);
            tvSteps = (TextView) itemView.findViewById(R.id.tv_individual_steps);
        }

    }
}
