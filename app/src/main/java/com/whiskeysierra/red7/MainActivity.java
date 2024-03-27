package com.whiskeysierra.red7;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private int numOfPlayers = 1;
    //TODO Нужно создать позднее, когда будет известно число игроков,
    // или добавить метод с изменением числа игроков,
    // или передать это число этой активити
    public Game game = new Game(numOfPlayers);
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CardAdapter adapter;
    private int itemWidth, itemHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemWidth = getResources().getDisplayMetrics().widthPixels / 7;
        itemHeight = (int) Math.round(itemWidth * 1.75);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CardAdapter(this, game.players.get(0).hand.cards,
                itemWidth, itemHeight);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}