package com.whiskeysierra.red7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LRCardAdapter extends RecyclerView.Adapter<LRCardAdapter.ViewHolder> {
    private ArrayList<Card> cards;
    private Context context;
    private int itemWidth, itemHeight;
    /**
     * false - left, true - right
     */
    private boolean type;

    public LRCardAdapter(Context context, ArrayList<Card> cards, int itemHeight, int itemWidth, boolean type) {
        this.context = context;
        this.cards = cards;
        this.itemHeight = itemHeight;
        this.itemWidth = itemWidth;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_lr_card_in_palette,
                parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemHeight;
        view.setLayoutParams(layoutParams);
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
        private TextView number_of_card;
        private RelativeLayout card_layout;
        private RelativeLayout card_layout_outer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number_of_card = itemView.findViewById(R.id.number_of_card);
            card_layout = itemView.findViewById(R.id.card_layout);
            card_layout_outer = itemView.findViewById(R.id.card_layout_outer);
        }

        public void bind(Card card) {
            if (card == null) {
                number_of_card.setText("0");
            } else {
                number_of_card.setText(String.valueOf(card.number));

                if (type)   number_of_card.setRotation(-90);
                else        number_of_card.setRotation(90);

                int color;
                switch (card.valueOfColor) {
                    case 1:
                        color = context.getResources().getColor(R.color.card_violet);
                        card_layout.setBackgroundColor(color);
                        break;
                    case 2:
                        color = context.getResources().getColor(R.color.card_indigo);
                        card_layout.setBackgroundColor(color);
                        break;
                    case 3:
                        color = context.getResources().getColor(R.color.card_blue);
                        card_layout.setBackgroundColor(color);
                        break;
                    case 4:
                        color = context.getResources().getColor(R.color.card_green);
                        card_layout.setBackgroundColor(color);
                        break;
                    case 5:
                        color = context.getResources().getColor(R.color.card_yellow);
                        card_layout.setBackgroundColor(color);
                        break;
                    case 6:
                        color = context.getResources().getColor(R.color.card_orange);
                        card_layout.setBackgroundColor(color);
                        break;
                    case 7:
                        color = context.getResources().getColor(R.color.card_red);
                        card_layout.setBackgroundColor(color);
                        break;
                }
            }
        }
    }
}

