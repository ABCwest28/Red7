package com.whiskeysierra.red7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CardAdapter extends ArrayAdapter<Card> {
    public CardAdapter(Context context, ArrayList<Card> cards) {
        super(context, 0, cards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Получение данных для текущей карты
        Card card = getItem(position);

        // Проверка наличия переиспользуемого представления, иначе создание нового
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_card_in_hand, parent, false);
        }

        RelativeLayout relativeLayout   = convertView.findViewById(R.id.card_layout);
        TextView textViewCard           = convertView.findViewById(R.id.number_of_card_layout);

        // Установка текста и фона
        if (card == null) {
            textViewCard.setText("0");
        }
        else {
            textViewCard.setText(String.valueOf(card.number));

            int color;
            switch (card.valueOfColor) {
                case 1:
                    color = getContext().getResources().getColor(R.color.card_violet);
                    relativeLayout.setBackgroundColor(color);
                    break;
                case 2:
                    color = getContext().getResources().getColor(R.color.card_indigo);
                    relativeLayout.setBackgroundColor(color);
                    break;
                case 3:
                    color = getContext().getResources().getColor(R.color.card_blue);
                    relativeLayout.setBackgroundColor(color);
                    break;
                case 4:
                    color = getContext().getResources().getColor(R.color.card_green);
                    relativeLayout.setBackgroundColor(color);
                    break;
                case 5:
                    color = getContext().getResources().getColor(R.color.card_yellow);
                    relativeLayout.setBackgroundColor(color);
                    break;
                case 6:
                    color = getContext().getResources().getColor(R.color.card_orange);
                    relativeLayout.setBackgroundColor(color);
                    break;
                case 7:
                    color = getContext().getResources().getColor(R.color.card_red);
                    relativeLayout.setBackgroundColor(color);
                    break;
            }
        }

        return convertView;
    }
}
