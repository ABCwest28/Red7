package com.whiskeysierra.red7;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private int numOfPlayers = 3;
    //TODO Нужно создать позднее, когда будет известно число игроков,
    // или добавить метод с изменением числа игроков,
    // или передать это число этой активити
    public Game game;
    private TextView handTopView, handRightView;
    private VerticalTextView handLeftViewInner;
    private LinearLayout handLeftViewOuter;
    private RecyclerView bottomRecyclerView, activeRecyclerView,
            leftRecyclerView, rightRecyclerView, topRecyclerView;
    private LinearLayoutManager bottomLayoutManager, activeLayoutManager,
            leftLayoutManager, rightLayoutManager, topLayoutManager;
    private BottomCardAdapter bottomCardAdapter;
    private ActiveCardAdapter activeCardAdapter;
    private TopCardAdapter topCardAdapter;
    private LRCardAdapter leftCardAdapter, rightCardAdapter;
    private int bottomCardWidth, bottomCardHeight, fieldCardWidth, fieldCardHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomRecyclerView  = findViewById(R.id.recyclerView);
        activeRecyclerView  = findViewById(R.id.active_recycler);
        topRecyclerView     = findViewById(R.id.top_recycler);
        leftRecyclerView    = findViewById(R.id.left_recycler);
        rightRecyclerView   = findViewById(R.id.right_recycler);

        handTopView         = findViewById(R.id.hand_top_view);
        handLeftViewInner   = findViewById(R.id.hand_left_view_inner);
        handLeftViewOuter   = findViewById(R.id.hand_left_view_outer);

        preparationByPlayers(numOfPlayers);
    }

    protected void preparationByPlayers(int numOfPlayers) {
        game = new Game(numOfPlayers);

        bottomCardWidth = getResources().getDisplayMetrics().widthPixels / 7;
        bottomCardHeight = (int) Math.round(bottomCardWidth * 1.75);
        fieldCardWidth = (int) Math.round(bottomCardWidth * 0.8);
        fieldCardHeight = (int) Math.round(bottomCardHeight * 0.8);


        bottomLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        bottomRecyclerView.setLayoutManager(bottomLayoutManager);
        bottomCardAdapter = new BottomCardAdapter(this, game.players.get(0).hand.cards,
                bottomCardWidth, bottomCardHeight);
        bottomRecyclerView.setAdapter(bottomCardAdapter);


        activeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        activeRecyclerView.setLayoutManager(activeLayoutManager);
        activeCardAdapter = new ActiveCardAdapter(this, game.players.get(0).palette.cards,
                fieldCardWidth, fieldCardHeight);
        activeRecyclerView.setAdapter(activeCardAdapter);


        switch (numOfPlayers) {
            case 2:
                topLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                        false);
                topRecyclerView.setLayoutManager(topLayoutManager);
                topCardAdapter = new TopCardAdapter(this, game.players.get(1).palette.cards,
                        fieldCardWidth, fieldCardHeight);
                topRecyclerView.setAdapter(topCardAdapter);

                handTopView.setText(String.valueOf(game.players.get(1).hand.cards.size()));
                handTopView.setWidth(fieldCardWidth + 20);
                break;
            case 3:
                leftLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                leftRecyclerView.setLayoutManager(leftLayoutManager);
                leftCardAdapter = new LRCardAdapter(this, game.players.get(0).palette.cards,
                        fieldCardWidth, fieldCardHeight);
                leftRecyclerView.setAdapter(leftCardAdapter);

                handLeftViewInner.setText(String.valueOf(game.players.get(1).hand.cards.size()));
                handLeftViewOuter.setMinimumHeight(fieldCardWidth + 20);

                break;
            case 4:
                break;
        }
    }
}