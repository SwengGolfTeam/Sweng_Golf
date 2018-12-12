package ch.epfl.sweng.swenggolf.statistics;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.tools.TwoFieldsViewHolder;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {
    private ArrayList<Integer> stats;

    public StatisticsAdapter(UserStats stats) {
        this.stats = new ArrayList<>(stats.values());
    }

    @NonNull
    @Override
    public StatisticsAdapter.StatisticsViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.statistics, viewGroup, false);
        return new StatisticsViewHolder(view, R.id.stats_text, R.id.stats_value);
    }

    @Override
    public void onBindViewHolder(
            @NonNull StatisticsViewHolder statisticsViewHolder, int i) {
        TextView text = (TextView) statisticsViewHolder.getFieldOne();
        text.setText(UserStats.Stats.values()[i].getText());

        TextView value = (TextView) statisticsViewHolder.getFieldTwo();
        value.setText(stats.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    /**
     * Representation of a row of the recyclerview.
     */
    public static class StatisticsViewHolder extends TwoFieldsViewHolder {


        public StatisticsViewHolder(@NonNull View itemView, int textId,
                                    int valueId) {
            super(itemView, textId, valueId);
        }

    }
}

