package com.whiskeysierra.red7;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private boolean isPaused = false;
    private boolean playerDidTurn = false;


    private int numOfPlayers;
    protected Game game;


    private LinearLayout rulesPileColor, rulesPileOuter;
    private TextView handTopView, rulesPileNumber;
    private VerticalTextView handLeftViewInner, handRightViewInner;
    private LinearLayout handLeftViewOuter, handRightViewOuter;
    private RecyclerView bottomRecyclerView, activeRecyclerView,
            leftRecyclerView, rightRecyclerView, topRecyclerView;
    private LinearLayoutManager bottomLayoutManager, activeLayoutManager,
            leftLayoutManager, rightLayoutManager, topLayoutManager;
    private BottomCardAdapter bottomCardAdapter;
    protected ActiveCardAdapter activeCardAdapter;
    private TopCardAdapter topCardAdapter;
    private LRCardAdapter leftCardAdapter, rightCardAdapter;
    private ItemTouchHelper itemTouchHelper;
    private Drawable deck_drawable, deck_lr_drawable, deck_active_drawable, deck_lr_active_drawable;
    protected Button button_do_turn, button_do_return, button_do_giveup, button_help;
    protected int bottomCardWidth, bottomCardHeight, fieldCardWidth, fieldCardHeight;


    protected Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPaused) {
                handler.postDelayed(this, 1000);
                return;
            }

            if (game.checkWin()) {
                runOnUiThread(() -> openPopupWindow(game.getIdWinner(game.rulesPile) == 0));
                return;
            }

            int idPlayerTurn = game.getNextId();

            if (idPlayerTurn == 0) {
                runOnUiThread(() -> {
                    bottomCardAdapter.menuClickListenersEnabled = true;
                    button_do_giveup.setVisibility(View.VISIBLE);
                });

                if (game.players.get(0).hand.cards.isEmpty()) {
                    game.players.get(0).isInGame = false;
                }
                else {
                    while (!playerDidTurn) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                playerDidTurn = false;
                runOnUiThread(() -> {
                    bottomCardAdapter.menuClickListenersEnabled = false;
                    runOnUiThread(() -> button_do_giveup.setVisibility(View.INVISIBLE));
                });

            } else {
                int turnResult = game.players.get(idPlayerTurn).doTurn();
                handleBotTurnResult(turnResult);
            }

            handleHighlightNextPlayer();

            handler.postDelayed(this, 2000);
        }
    };

    protected void handleBotTurnResult(int turnResult) {
        runOnUiThread(() -> {
            switch (turnResult) {
                case 0:
                    //TODO вылет игрока
                    Toast.makeText(MainActivity.this, "Player has no cards", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(MainActivity.this, "Player has no choice", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    notifyAdaptersAndSetNumOfCardsInHand(numOfPlayers);
                    break;
                case 2:
                case 3:
                    setCardToRulesPile(game.rulesPile);
                    notifyAdaptersAndSetNumOfCardsInHand(numOfPlayers);
                    break;
            }
        });
    }

    protected void handleHighlightNextPlayer() {
        runOnUiThread(() -> {
            switch (game.getNextId()) {
                case 0:
                    switch (numOfPlayers) {
                        case 2:
                            handTopView.setBackground(deck_drawable);
                            break;
                        case 3:
                            handLeftViewOuter.setBackground(deck_lr_drawable);
                            handRightViewOuter.setBackground(deck_lr_drawable);
                            break;
                        case 4:
                            handTopView.setBackground(deck_drawable);
                            handLeftViewOuter.setBackground(deck_lr_drawable);
                            handRightViewOuter.setBackground(deck_lr_drawable);
                            break;
                    }
                    break;
                case 1:
                    switch (numOfPlayers) {
                        case 2:
                            handTopView.setBackground(deck_active_drawable);
                            break;
                        case 3:
                            handLeftViewOuter.setBackground(deck_lr_active_drawable);
                            handRightViewOuter.setBackground(deck_lr_drawable);
                            break;
                        case 4:
                            handLeftViewOuter.setBackground(deck_lr_active_drawable);
                            handTopView.setBackground(deck_drawable);
                            handRightViewOuter.setBackground(deck_lr_drawable);
                            break;
                    }
                    break;
                case 2:
                    switch (numOfPlayers) {
                        case 3:
                            handLeftViewOuter.setBackground(deck_lr_drawable);
                            handRightViewOuter.setBackground(deck_lr_active_drawable);
                            break;
                        case 4:
                            handLeftViewOuter.setBackground(deck_lr_drawable);
                            handTopView.setBackground(deck_active_drawable);
                            handRightViewOuter.setBackground(deck_lr_drawable);
                            break;
                    }
                    break;
                case 3:
                    handLeftViewOuter.setBackground(deck_lr_drawable);
                    handTopView.setBackground(deck_drawable);
                    handRightViewOuter.setBackground(deck_lr_active_drawable);
                    break;
            }
        });
    }


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

        button_do_turn      = findViewById(R.id.button_do_turn);
        button_do_return    = findViewById(R.id.button_do_return);
        button_do_giveup    = findViewById(R.id.button_do_giveup);
        button_help         = findViewById(R.id.button_help);

        deck_drawable           = ContextCompat.getDrawable(this, R.drawable.deck_drawable);
        deck_lr_drawable        = ContextCompat.getDrawable(this, R.drawable.deck_lr_drawable);
        deck_active_drawable    = ContextCompat.getDrawable(this, R.drawable.deck_active_drawable);
        deck_lr_active_drawable = ContextCompat.getDrawable(this, R.drawable.deck_lr_active_drawable);

        preparationByPlayers(numOfPlayers);
        gameProcess();
    }

    protected void preparationByPlayers(int numOfPlayers) {
        button_do_turn.setVisibility(View.INVISIBLE);
        button_do_return.setVisibility(View.INVISIBLE);
        button_do_giveup.setVisibility(View.INVISIBLE);

        game = new Game(numOfPlayers);

        bottomCardWidth = getResources().getDisplayMetrics().widthPixels / 7;
        bottomCardHeight = (int) Math.round(bottomCardWidth * 1.75);
        fieldCardWidth = (int) Math.round(bottomCardWidth * 0.8);
        fieldCardHeight = (int) Math.round(bottomCardHeight * 0.8);

        rulesPileOuter.setMinimumWidth(fieldCardWidth);
        rulesPileOuter.setMinimumHeight(fieldCardHeight);

        setCardToRulesPile(game.rulesPile);

        activeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        activeRecyclerView.setLayoutManager(activeLayoutManager);
        activeCardAdapter = new ActiveCardAdapter(this, game.players.get(0).palette.cards,
                fieldCardWidth, fieldCardHeight);
        activeRecyclerView.setAdapter(activeCardAdapter);


        bottomLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        bottomRecyclerView.setLayoutManager(bottomLayoutManager);
        bottomCardAdapter = new BottomCardAdapter(this, game.players.get(0).hand.cards,
                bottomCardWidth, bottomCardHeight, this);
        bottomRecyclerView.setAdapter(bottomCardAdapter);


        itemTouchHelper = new ItemTouchHelper(new CardItemTouchHelper(bottomCardAdapter));
        itemTouchHelper.attachToRecyclerView(bottomRecyclerView);


        button_do_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_return();
            }
        });

        button_do_turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_turn();
            }
        });

        button_do_giveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.players.get(0).isInGame = false;
                playerDidTurn = true;
            }
        });

        button_help.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.help_popup_layout, null);

            // Указываем размеры и прочие параметры
            int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            popupWindow.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
        });


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


    private void gameProcess() {
        handler.post(gameRunnable);
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        handler.removeCallbacks(gameRunnable);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        handler.post(gameRunnable);
    }


    protected void notifyAdaptersAndSetNumOfCardsInHand(int numOfPlayers) {
        runOnUiThread(() -> {
            switch (numOfPlayers) {
                case 2:
                    topCardAdapter.notifyDataSetChanged();

                    handTopView.setText(String.valueOf(game.players.get(1).hand.cards.size()));

                    break;
                case 3:
                    leftCardAdapter.notifyDataSetChanged();
                    rightCardAdapter.notifyDataSetChanged();

                    handLeftViewInner.setText(String.valueOf(game.players.get(1).hand.cards.size()));
                    handRightViewInner.setText(String.valueOf(game.players.get(2).hand.cards.size()));

                    break;
                case 4:
                    topCardAdapter.notifyDataSetChanged();
                    leftCardAdapter.notifyDataSetChanged();
                    rightCardAdapter.notifyDataSetChanged();

                    handTopView.setText(String.valueOf(game.players.get(2).hand.cards.size()));
                    handLeftViewInner.setText(String.valueOf(game.players.get(1).hand.cards.size()));
                    handRightViewInner.setText(String.valueOf(game.players.get(3).hand.cards.size()));

                    break;
            }

            activeCardAdapter.notifyDataSetChanged();
            bottomCardAdapter.notifyDataSetChanged();
        });
    }

    protected void openPopupWindow(boolean win) {
        runOnUiThread(() -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            int resourse = win ? R.layout.win_popup_layout : R.layout.lose_popup_layout;

            View popupView = inflater.inflate(resourse, null);

            // Указываем размеры и прочие параметры
            int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

            Button button_back_to_menu = popupView.findViewById(R.id.button_back_to_menu);
            button_back_to_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    Intent intent = new Intent(MainActivity.this, StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });

            popupWindow.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);

        });
    }

    protected void do_turn() {
        if (game.intentDiscardCard != null) game.rulesPile = game.intentDiscardCard;

        activeCardAdapter.notifyDataSetChanged();
        bottomCardAdapter.notifyDataSetChanged();
        setCardToRulesPile(game.rulesPile);
        game.intentPlayCard     = null;
        game.intentDiscardCard  = null;

        button_do_turn.     setVisibility(View.INVISIBLE);
        button_do_return.   setVisibility(View.INVISIBLE);

        playerDidTurn = true;
    }

    protected void do_return() {
        if (game.intentPlayCard != null) {
            game.players.get(0).palette.removeCard(game.intentPlayCard);
            game.players.get(0).hand.addCard(game.intentPlayCard);
            game.intentPlayCard = null;
        }
        if (game.intentDiscardCard != null) {
            game.players.get(0).hand.addCard(game.intentDiscardCard);
            game.intentDiscardCard = null;
        }

        button_do_return.setVisibility(View.INVISIBLE);
        button_do_turn.setVisibility(View.INVISIBLE);
        activeCardAdapter.notifyDataSetChanged();
        bottomCardAdapter.notifyDataSetChanged();
        setCardToRulesPile(game.rulesPile);
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
}