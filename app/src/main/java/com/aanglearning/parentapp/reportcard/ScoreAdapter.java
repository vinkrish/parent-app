package com.aanglearning.parentapp.reportcard;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.model.Student;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 16-11-2017.
 */

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {
    private Context mContext;
    private List<StudentScore> items;
    private StudentScore selectedSch;

    ScoreAdapter(Context context, List<StudentScore> items) {
        this.mContext = context;
        this.items = items;
    }

    List<StudentScore> getDataSet() {
        return items;
    }

    @UiThread
    void setDataSet(List<StudentScore> items) {
        this.items = items;
        this.selectedSch = new StudentScore();
        notifyDataSetChanged();
    }

    @UiThread
    void selectedItemChanged(int newPosition, StudentScore selectedSch) {
        this.selectedSch = selectedSch;
        notifyItemChanged(newPosition);
    }

    @Override
    public ScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ScoreAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ScoreAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view) LinearLayout cardView;
        @BindView(R.id.sch_name) TextView schName;
        @BindView(R.id.max_score) TextView maxScore;
        @BindView(R.id.score) TextView scoreTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final StudentScore score) {
            schName.setText(score.getSchName());
            maxScore.setText(String.format(Locale.ENGLISH, "%d", (int)score.getMaxMark()));
            StringBuilder sbScore = new StringBuilder();
            if (String.valueOf(score.getMark()).equals("0.0")) {
                if (score.getGrade().equals("")) sbScore.append("- ");
                else sbScore.append(score.getGrade());
            } else {
                DecimalFormat format = new DecimalFormat("#.#");
                sbScore.append(String.valueOf(format.format(score.getMark())));
                if (!score.getGrade().equals("")) sbScore.append(" / ").append(score.getGrade());
            }
            scoreTv.setText(sbScore.toString());

            if (score.getSchId() == selectedSch.getSchId()) {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.default_white));
            }
        }
    }
}
