package com.whiskeysierra.red7;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private int numOfPlayers = 1;
    private ListView handListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO Нужно создать позднее, когда будет известно число игроков,
        // или добавить метод с изменением числа игроков,
        // или передать это число этой активити
        Game game = new Game(numOfPlayers);

        handListView = findViewById(R.id.hand_listView);

        CardAdapter cardAdapter = new CardAdapter(MainActivity.this, game.players.get(0).hand.cards);

        handListView.setAdapter(cardAdapter);

    }
}