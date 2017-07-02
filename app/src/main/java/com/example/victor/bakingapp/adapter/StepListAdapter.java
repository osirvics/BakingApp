package com.example.victor.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.bakingapp.R;
import com.example.victor.bakingapp.interfaces.OnItemClickListener;
import com.example.victor.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Victor on 27/06/2017.
 */

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListHolder> {
    private ArrayList<Step> steps;
    private Context context;
    OnItemClickListener clickLister;
    public StepListAdapter (Context context, ArrayList<Step> steps, OnItemClickListener clickLister){
        this.steps = steps;
        this.context = context;
        this.clickLister = clickLister;
    }

    @Override
    public StepListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_list_step, parent, false);
        return new StepListHolder(view);
    }

    @Override
    public void onBindViewHolder(StepListHolder holder, int position) {
        Step step = steps.get(position);
        holder.stepName.setText(step.getShortDescription());
        holder.mIndex.setText(String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return (steps == null) ? 0 : steps.size();
    }

    public class StepListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_short_description)
        TextView stepName;
        @BindView(R.id.index)
        TextView mIndex;
        public StepListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickLister.OnItemClick(getLayoutPosition());
        }
    }
}
