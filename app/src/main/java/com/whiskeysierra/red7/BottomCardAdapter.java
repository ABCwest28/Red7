package com.whiskeysierra.red7;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class BottomCardAdapter extends RecyclerView.Adapter<BottomCardAdapter.ViewHolder> {
    protected ArrayList<Card> cards;
    protected Game game;
    protected ActiveCardAdapter aca;
    protected Button button_do_turn;
    protected MainActivity mainActivity;
    private Context context;
    private int itemWidth, itemHeight;
    protected boolean menuClickListenersEnabled = true;

    public BottomCardAdapter(Context context, ArrayList<Card> cards, int itemWidth, int itemHeight, MainActivity mainActivity) {
        this.context = context;
        this.cards = cards;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;

        this.mainActivity = mainActivity;
        this.game = mainActivity.game;
        this.aca = mainActivity.activeCardAdapter;
        this.button_do_turn = mainActivity.button_do_turn;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
        notifyItemInserted(cards.size() - 1);
    }

    public Card removeCard(int position) {
        Card card = cards.remove(position);
        notifyItemRemoved(position);
        return card;
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(cards, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(cards, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_card_in_hand,
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

        holder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            MenuInflater inflater = ((Activity) context).getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);

            if (menuClickListenersEnabled) {
                menu.findItem(R.id.action_play).setOnMenuItemClickListener(item -> {

                    mainActivity.button_do_return.setVisibility(View.VISIBLE);

                    if (game.intentPlayCard != null) {
                        game.players.get(0).palette.removeCard(game.intentPlayCard);
                        game.players.get(0).hand.addCard(game.intentPlayCard);
                    }

                    game.intentPlayCard = card;

                    if (game.intentDiscardCard == null && game.players.get(0).tryToPlayCard(game.intentPlayCard)) {
                        // Toast.makeText(context, "P", Toast.LENGTH_LONG).show();
                        button_do_turn.setVisibility(View.VISIBLE);
                    }

                    else if (game.intentDiscardCard != null &&
                            game.players.get(0).tryToPlayThenDiscardCard(game.intentPlayCard, game.intentDiscardCard)) {
                        // Toast.makeText(context, "PD", Toast.LENGTH_LONG).show();
                        button_do_turn.setVisibility(View.VISIBLE);
                    }

                    else button_do_turn.setVisibility(View.INVISIBLE);

                    game.players.get(0).playCardFromHandToPalette(card);

                    notifyDataSetChanged();
                    aca.notifyDataSetChanged();

                    return true;
                });


                menu.findItem(R.id.action_discard).setOnMenuItemClickListener(item -> {

                    mainActivity.button_do_return.setVisibility(View.VISIBLE);

                    if (game.intentDiscardCard != null) {
                        game.players.get(0).hand.addCard(game.intentDiscardCard);
                    }

                    game.intentDiscardCard = card;

                    if (game.intentPlayCard == null &&
                            game.players.get(0).tryToDiscardCard(game.intentDiscardCard)) {
                        // Toast.makeText(context, "D", Toast.LENGTH_LONG).show();
                        button_do_turn.setVisibility(View.VISIBLE);
                    }

                    else if (game.intentPlayCard != null && game.players.get(0).
                            tryToPlayThenDiscardCard(game.intentPlayCard, game.intentDiscardCard)) {
                        // Toast.makeText(context, "PD", Toast.LENGTH_LONG).show();
                        button_do_turn.setVisibility(View.VISIBLE);
                    }

                    else button_do_turn.setVisibility(View.INVISIBLE);

                    mainActivity.setCardToRulesPile(game.intentDiscardCard);
                    game.players.get(0).hand.removeCard(game.intentDiscardCard);

                    notifyDataSetChanged();

                    return true;
                });
            }
        });

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
