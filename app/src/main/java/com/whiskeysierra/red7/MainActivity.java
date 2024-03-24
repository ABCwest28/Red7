package com.whiskeysierra.red7;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private int numOfPlayers = 1;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO Нужно создать позднее, когда будет известно число игроков,
        // или добавить метод с изменением числа игроков,
        // или передать это число этой активити
        Game game = new Game(numOfPlayers);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CardAdapter adapter = new CardAdapter(this, game.players.get(0).hand.cards);
        recyclerView.setAdapter(adapter);

    }
}