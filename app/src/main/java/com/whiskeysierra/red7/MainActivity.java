package com.whiskeysierra.red7;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private int numOfPlayers = 1;
    //TODO Нужно создать позднее, когда будет известно число игроков,
    // или добавить метод с изменением числа игроков,
    // или передать это число этой активити
    public Game game;
    private RecyclerView bottomRecyclerView, activeRecyclerView,
            leftRecyclerView, rightRecyclerView, topRecyclerView;
    private LinearLayoutManager bottomLayoutManager, activeLayoutManager,
            leftLayoutManager, rightLayoutManager, topLayoutManager;
    private BottomCardAdapter bottomCardAdapter;
    private ActiveCardAdapter activeCardAdapter;

    private int bottomCardWidth, bottomCardHeight, activeCardWidth, activeCardHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preparationByPlayers(numOfPlayers);
    }

    protected void preparationByPlayers(int numOfPlayers) {
        game = new Game(numOfPlayers);

        bottomCardWidth = getResources().getDisplayMetrics().widthPixels / 7;
        bottomCardHeight = (int) Math.round(bottomCardWidth * 1.75);
        activeCardWidth = (int) Math.round(bottomCardWidth * 0.8);
        activeCardHeight = (int) Math.round(bottomCardHeight * 0.8);

        bottomRecyclerView = findViewById(R.id.recyclerView);
        bottomLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        bottomRecyclerView.setLayoutManager(bottomLayoutManager);
        bottomCardAdapter = new BottomCardAdapter(this, game.players.get(0).hand.cards,
                bottomCardWidth, bottomCardHeight);
        bottomRecyclerView.setAdapter(bottomCardAdapter);

        activeRecyclerView = findViewById(R.id.active_recycler);
        activeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        activeRecyclerView.setLayoutManager(activeLayoutManager);
        activeCardAdapter = new ActiveCardAdapter(this, game.players.get(0).palette.cards,
                activeCardWidth, activeCardHeight);
        activeRecyclerView.setAdapter(activeCardAdapter);


        switch (numOfPlayers) {
            case 2:

        }
    }
}