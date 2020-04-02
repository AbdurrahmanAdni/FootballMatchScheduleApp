package com.example.bolasepak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MatchScheduleAdapter extends RecyclerView.Adapter<MatchScheduleAdapter.MatchScheduleViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<MatchSchedule> mMatchScheduleList;
    private ArrayList<MatchSchedule> mMatchSchedulesListFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public MatchScheduleAdapter(Context context, ArrayList<MatchSchedule> matchScheduleList){
        mContext = context;
        mMatchScheduleList = matchScheduleList;
        mMatchSchedulesListFull = new ArrayList<>(matchScheduleList);
    }

    @NonNull
    @Override
    public MatchScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.match_card, parent, false);
        return new MatchScheduleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchScheduleViewHolder holder, int position) {
        MatchSchedule currentItem = mMatchScheduleList.get(position);

        String hTeamName = currentItem.getHomeTeamName();
        String hImageUrl = currentItem.getHomeImageURL();
        String hTeamScore = currentItem.getHomeScore();
        String d = currentItem.getDate();
        String aTeamName = currentItem.getAwayTeamName();
        String aImageUrl = currentItem.getAwayImageURL();
        String aTeamScore = currentItem.getAwayScore();

        holder.mHomeTeamName.setText(hTeamName);
        holder.mHomeTeamScore.setText(hTeamScore);
        holder.mDate.setText(d);
        holder.mAwayTeamName.setText(aTeamName);
        holder.mAwayTeamScore.setText(aTeamScore);

        Picasso.get().load(hImageUrl).fit().centerInside().into(holder.mHomeTeamImage);
        Picasso.get().load(aImageUrl).fit().centerInside().into(holder.mAwayTeamImage);

    }

    @Override
    public int getItemCount() {
        return mMatchScheduleList.size();
    }

    public Filter getFilter(){
        return matchFilter;
    }

    public Filter matchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MatchSchedule> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mMatchSchedulesListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (MatchSchedule item : mMatchSchedulesListFull){
                    if ((item.getAwayTeamName().toLowerCase().contains(filterPattern)) ||
                            (item.getHomeTeamName().toLowerCase().contains(filterPattern))){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mMatchScheduleList.clear();
            mMatchScheduleList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public class MatchScheduleViewHolder extends RecyclerView.ViewHolder{

        public TextView mHomeTeamName;
        public TextView mAwayTeamName;
        public TextView mDate;
        public ImageView mHomeTeamImage;
        public ImageView mAwayTeamImage;
        public TextView mHomeTeamScore;
        public TextView mAwayTeamScore;

        public MatchScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            mHomeTeamName = itemView.findViewById(R.id.homeTeamName);
            mHomeTeamScore = itemView.findViewById(R.id.homeScore);
            mHomeTeamImage = (ImageView) itemView.findViewById(R.id.homeTeamLogo);
            mDate = itemView.findViewById(R.id.date);
            mAwayTeamName = itemView.findViewById(R.id.awayTeamName);
            mAwayTeamScore = itemView.findViewById(R.id.awayScore);
            mAwayTeamImage = (ImageView) itemView.findViewById(R.id.awayTeamLogo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
