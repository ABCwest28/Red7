package com.whiskeysierra.red7;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private int numOfPlayers;
    public Game game;
    private LinearLayout rulesPileColor, rulesPileOuter;
    private TextView handTopView, rulesPileNumber;
    private VerticalTextView handLeftViewInner, handRightViewInner;
    private LinearLayout handLeftViewOuter, handRightViewOuter;
    private RecyclerView bottomRecyclerView, activeRecyclerView,
            leftRecyclerView, rightRecyclerView, topRecyclerView;
    private LinearLayoutManager bottomLayoutManager, activeLayoutManager,
            leftLayoutManager, rightLayoutManager, topLayoutManager;
    private BottomCardAdapter bottomCardAdapter;
    private ActiveCardAdapter activeCardAdapter;
    private TopCardAdapter topCardAdapter;
    private LRCardAdapter leftCardAdapter, rightCardAdapter;
    private Drawable deck_drawable, deck_lr_drawable, deck_active_drawable, deck_lr_active_drawable;
    private int bottomCardWidth, bottomCardHeight, fieldCardWidth, fieldCardHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numOfPlayers = getIntent().getIntExtra("numOfPlayers", 4);

        bottomRecyclerView  = findViewById(R.id.recyclerView);
        activeRecyclerView  = findViewById(R.id.active_recycler);
        topRecyclerView     = findViewById(R.id.top_recycler);
        leftRecyclerView    = findViewById(R.id.left_recycler);
        rightRecyclerView   = findViewById(R.id.right_recycler);

        handTopView         = findViewById(R.id.hand_top_view);
        handLeftViewInner   = findViewById(R.id.hand_left_view_inner);
        handLeftViewOuter   = findViewById(R.id.hand_left_view_outer);
        handRightViewInner  = findViewById(R.id.hand_right_view_inner);
        handRightViewOuter  = findViewById(R.id.hand_right_view_outer);

        rulesPileOuter      = findViewById(R.id.rules_pile_outer);
        rulesPileColor      = findViewById(R.id.rules_pile_color);
        rulesPileNumber     = findViewById(R.id.rules_pile_number);

        deck_drawable           = ContextCompat.getDrawable(this, R.drawable.deck_drawable);
        deck_lr_drawable        = ContextCompat.getDrawable(this, R.drawable.deck_lr_drawable);
        deck_active_drawable    = ContextCompat.getDrawable(this, R.drawable.deck_active_drawable);
        deck_lr_active_drawable = ContextCompat.getDrawable(this, R.drawable.deck_lr_active_drawable);

        preparationByPlayers(numOfPlayers);
        setShowTooltipHandCards();
        gameProcess();
    }

    protected void preparationByPlayers(int numOfPlayers) {
        game = new Game(numOfPlayers);

        bottomCardWidth = getResources().getDisplayMetrics().widthPixels / 7;
        bottomCardHeight = (int) Math.round(bottomCardWidth * 1.75);
        fieldCardWidth = (int) Math.round(bottomCardWidth * 0.8);
        fieldCardHeight = (int) Math.round(bottomCardHeight * 0.8);

        rulesPileOuter.setMinimumWidth(fieldCardWidth);
        rulesPileOuter.setMinimumHeight(fieldCardHeight);

        setCardToRulesPile(game.rulesPile);

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
                leftCardAdapter = new LRCardAdapter(this, game.players.get(1).palette.cards,
                        fieldCardWidth, fieldCardHeight, false);
                leftRecyclerView.setAdapter(leftCardAdapter);

                handLeftViewInner.setText(String.valueOf(game.players.get(1).hand.cards.size()));
                handLeftViewOuter.setMinimumHeight(fieldCardWidth + 20);


                rightLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                rightRecyclerView.setLayoutManager(rightLayoutManager);
                rightCardAdapter = new LRCardAdapter(this, game.players.get(2).palette.cards,
                        fieldCardWidth, fieldCardHeight, true);
                rightRecyclerView.setAdapter(rightCardAdapter);

                handRightViewInner.setText(String.valueOf(game.players.get(2).hand.cards.size()));
                handRightViewOuter.setMinimumHeight(fieldCardWidth + 20);

                break;
            case 4:
                topLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                        false);
                topRecyclerView.setLayoutManager(topLayoutManager);
                topCardAdapter = new TopCardAdapter(this, game.players.get(2).palette.cards,
                        fieldCardWidth, fieldCardHeight);
                topRecyclerView.setAdapter(topCardAdapter);

                handTopView.setText(String.valueOf(game.players.get(2).hand.cards.size()));
                handTopView.setWidth(fieldCardWidth + 20);

                leftLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                leftRecyclerView.setLayoutManager(leftLayoutManager);
                leftCardAdapter = new LRCardAdapter(this, game.players.get(1).palette.cards,
                        fieldCardWidth, fieldCardHeight, false);
                leftRecyclerView.setAdapter(leftCardAdapter);

                handLeftViewInner.setText(String.valueOf(game.players.get(1).hand.cards.size()));
                handLeftViewOuter.setMinimumHeight(fieldCardWidth + 20);


                rightLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                rightRecyclerView.setLayoutManager(rightLayoutManager);
                rightCardAdapter = new LRCardAdapter(this, game.players.get(3).palette.cards,
                        fieldCardWidth, fieldCardHeight, true);
                rightRecyclerView.setAdapter(rightCardAdapter);

                handRightViewInner.setText(String.valueOf(game.players.get(3).hand.cards.size()));
                handRightViewOuter.setMinimumHeight(fieldCardWidth + 20);
                break;
        }
    }

    protected void setCardToRulesPile(@NonNull Card card) {
        rulesPileNumber.setText(String.valueOf(card.number));

        int color;
        switch (card.valueOfColor) {
            case 1:
                color = this.getResources().getColor(R.color.card_violet);
                rulesPileColor.setBackgroundColor(color);
                break;
            case 2:
                color = this.getResources().getColor(R.color.card_indigo);
                rulesPileColor.setBackgroundColor(color);
                break;
            case 3:
                color = this.getResources().getColor(R.color.card_blue);
                rulesPileColor.setBackgroundColor(color);
                break;
            case 4:
                color = this.getResources().getColor(R.color.card_green);
                rulesPileColor.setBackgroundColor(color);
                break;
            case 5:
                color = this.getResources().getColor(R.color.card_yellow);
                rulesPileColor.setBackgroundColor(color);
                break;
            case 6:
                color = this.getResources().getColor(R.color.card_orange);
                rulesPileColor.setBackgroundColor(color);
                break;
            case 7:
                color = this.getResources().getColor(R.color.card_red);
                rulesPileColor.setBackgroundColor(color);
                break;
        }
    }

    //TODO нужно указать кол-во игроков
    protected void gameProcess() {
        Thread myThread = new Thread() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (game.checkWin()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Win", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        int idPlayerTurn = game.getNextId();

                        if (idPlayerTurn == 999) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Your Turn", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        else {
                            int turnResult = game.players.get(idPlayerTurn).doTurn();

                            switch (turnResult) {
                                case 0:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Player has no cards", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;

                                case 4:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Player has no choice", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;

                                case 1:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Player played card", Toast.LENGTH_SHORT).show();
                                            topCardAdapter.notifyDataSetChanged();
                                            leftCardAdapter.notifyDataSetChanged();
                                            rightCardAdapter.notifyDataSetChanged();
                                            activeCardAdapter.notifyDataSetChanged();
                                            bottomCardAdapter.notifyDataSetChanged();

                                            handLeftViewInner.setText(String.valueOf(game.players.get(1).hand.cards.size()));
                                            handTopView.setText(String.valueOf(game.players.get(2).hand.cards.size()));
                                            handRightViewInner.setText(String.valueOf(game.players.get(3).hand.cards.size()));
                                        }
                                    });
                                    break;
                                case 2:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Player discarded card", Toast.LENGTH_SHORT).show();
                                            setCardToRulesPile(game.rulesPile);
                                            bottomCardAdapter.notifyDataSetChanged();

                                            handLeftViewInner.setText(String.valueOf(game.players.get(1).hand.cards.size()));
                                            handTopView.setText(String.valueOf(game.players.get(2).hand.cards.size()));
                                            handRightViewInner.setText(String.valueOf(game.players.get(3).hand.cards.size()));
                                        }
                                    });
                                    break;
                                case 3:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Player played and discarded card", Toast.LENGTH_SHORT).show();
                                            topCardAdapter.notifyDataSetChanged();
                                            leftCardAdapter.notifyDataSetChanged();
                                            rightCardAdapter.notifyDataSetChanged();
                                            activeCardAdapter.notifyDataSetChanged();
                                            bottomCardAdapter.notifyDataSetChanged();

                                            setCardToRulesPile(game.rulesPile);

                                            handLeftViewInner.setText(String.valueOf(game.players.get(1).hand.cards.size()));
                                            handTopView.setText(String.valueOf(game.players.get(2).hand.cards.size()));
                                            handRightViewInner.setText(String.valueOf(game.players.get(3).hand.cards.size()));
                                        }
                                    });
                                    break;
                            }
                        }
                        // Подсветка следущего игрока
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int id = game.getNextId();
                                switch (id) {
                                    case 0:
                                        handLeftViewOuter.setBackground(deck_lr_drawable);
                                        handRightViewOuter.setBackground(deck_lr_drawable);
                                        handTopView.setBackground(deck_drawable);
                                        break;
                                    case 1:
                                        handLeftViewOuter.setBackground(deck_lr_active_drawable);
                                        handRightViewOuter.setBackground(deck_lr_drawable);
                                        handTopView.setBackground(deck_drawable);
                                        break;
                                    case 2:
                                        handLeftViewOuter.setBackground(deck_lr_drawable);
                                        handRightViewOuter.setBackground(deck_lr_drawable);
                                        handTopView.setBackground(deck_active_drawable);
                                        break;
                                    case 3:
                                        handLeftViewOuter.setBackground(deck_lr_drawable);
                                        handRightViewOuter.setBackground(deck_lr_active_drawable);
                                        handTopView.setBackground(deck_drawable);
                                        break;
                                }
                            }
                        });
                    }
                } while (false);
            }
        };
        myThread.start();
    }

    protected void setShowTooltipHandCards() {
        handTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = game.players.get(2).hand.cards.toString();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
        handLeftViewOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = game.players.get(1).hand.cards.toString();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
        handRightViewOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = game.players.get(3).hand.cards.toString();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
        rulesPileOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameProcess();
            }
        });
    }
}