package com.example.silver_screen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TriviaListAdapter extends RecyclerView.Adapter<TriviaListAdapter.TriviaListViewHolder> {

    private List<TriviaListModel> triviaListModels;
    private OnItemClickedListener onItemClickedListener;

//    public TriviaListAdapter(TriviaListFragment triviaListFragment) {
//
//    }

    public void setTriviaListModels(List<TriviaListModel> triviaListModels) {
        this.triviaListModels = triviaListModels;
    }

    public TriviaListAdapter(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public TriviaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trivia, parent, false);
        return new TriviaListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TriviaListAdapter.TriviaListViewHolder holder, int position) {
        TriviaListModel model = triviaListModels.get(position);
        holder.title.setText(model.getTitle());
        Glide.with(holder.itemView).load(model.getImage()).into(holder.triviaImage);
    }

    @Override
    public int getItemCount() {
        return triviaListModels == null ? 0 : triviaListModels.size();
    }

    public class TriviaListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView triviaImage;
        private ConstraintLayout constraintLayout;

        public TriviaListViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.triviaTextList);
            triviaImage = itemView.findViewById(R.id.triviaImageList);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickedListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(int position);
    }
}