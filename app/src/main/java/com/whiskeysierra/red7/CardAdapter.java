package com.whiskeysierra.red7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private ArrayList<Card> cards;
    private Context context;

    public CardAdapter(Context context, ArrayList<Card> cards) {
        this.context = context;
        this.cards = cards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_card_in_hand, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.bind(card);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCard;
        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCard = itemView.findViewById(R.id.number_of_card_layout);
            relativeLayout = itemView.findViewById(R.id.card_layout);
        }

        public void bind(Card card) {
            if (card == null) {
                textViewCard.setText("0");
            } else {
                textViewCard.setText(String.valueOf(card.number));

                int color;
                switch (card.valueOfColor) {
                    case 1:
                        color = context.getResources().getColor(R.color.card_violet);
                        relativeLayout.setBackgroundColor(color);
                        break;
                    case 2:
                        color = context.getResources().getColor(R.color.card_indigo);
                        relativeLayout.setBackgroundColor(color);
                        break;
                    case 3:
                        color = context.getResources().getColor(R.color.card_blue);
                        relativeLayout.setBackgroundColor(color);
                        break;
                    case 4:
                        color = context.getResources().getColor(R.color.card_green);
                        relativeLayout.setBackgroundColor(color);
                        break;
                    case 5:
                        color = context.getResources().getColor(R.color.card_yellow);
                        relativeLayout.setBackgroundColor(color);
                        break;
                    case 6:
                        color = context.getResources().getColor(R.color.card_orange);
                        relativeLayout.setBackgroundColor(color);
                        break;
                    case 7:
                        color = context.getResources().getColor(R.color.card_red);
                        relativeLayout.setBackgroundColor(color);
                        break;
                }
            }
        }
    }
}
