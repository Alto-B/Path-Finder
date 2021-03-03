package com.comp2601.assignment2;

import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

public class Game{

    public static final int COL_SIZE = 10;
    public static final int ROW_SIZE = 12;

    private final Button[][] board;
    private Button placedStart;
    private Button placedEnd;

    private boolean done;

    public Game() {
        board = new Button[ROW_SIZE][COL_SIZE];
        placedStart = null;
        placedEnd = null;
        done = false;
    }

    public boolean isDone() { return done; }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isPlacedStart() {
        return placedStart != null;
    }

    public Button getPlacedStart() {return placedStart;}

    public void setPlacedStart(Button placedStart) {
        this.placedStart = placedStart;
    }

    public boolean isPlacedEnd() {
        return placedEnd != null;
    }

    public void setPlacedEnd(Button placedEnd) {
        this.placedEnd = placedEnd;
    }

    public void setPosition(int row, int col, Button val){
        board[row][col] = val;
    }

    public String getPosition(int row, int col){
        return board[row][col].getText().toString();
    }

    public Button[][] getBoard() {
        return board;
    }


}
