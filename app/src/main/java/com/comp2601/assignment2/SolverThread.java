package com.comp2601.assignment2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SolverThread extends HandlerThread {

    private static final String TAG = "Solver";

    private Handler handler;

    public SolverThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        handler = new Handler(getLooper());
    }

    public Handler getHandler() {
        return handler;
    }

    static class SolveBoard implements Runnable{

        private final Game game;
        private final Queue<Button> queue;
        private final Handler mainHandler;
        private final Context context;
        private final Button solveBtn;

        public SolveBoard(Game game, Context context, Button solveBtn){
            this.game = game;
            this.context = context;
            this.solveBtn = solveBtn;
            queue = new LinkedList<>();
            mainHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void run() {
            Button startPos = game.getPlacedStart();
            queue.add(startPos);

            while(!queue.isEmpty()){

                Button popped = queue.poll();
                // current button position
                String currentPos = popped.getTag().toString();
                int rowPos = Integer.parseInt(currentPos.substring(3,currentPos.indexOf('_')));
                int colPos = Integer.parseInt(currentPos.substring(currentPos.indexOf('_') + 1));

                // current button value
                String val = popped.getText().toString();
                Log.i(TAG, "Current pos: " + rowPos + colPos + " val: " + val);

                if(val.equals("D")){
                    break;
                }else if(val.equals("E") || val.equals("S")) {
                    try{
                        Thread.sleep(10);
                        updateUI(rowPos, colPos, context);
                        List<Button> nextSet = findNextPath(rowPos, colPos, game.getBoard());
                        queue.addAll(nextSet);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            game.setDone(true);
            mainHandler.post(() -> {
                solveBtn.setText("Restart");
                solveBtn.setEnabled(true);
            });
        }

        private void updateUI(int row, int col, Context context){
            mainHandler.post(() -> {
                Button[][] board = game.getBoard();
                if(!board[row][col].equals(game.getPlacedStart())){

                    board[row][col].setText("V");
                    board[row][col].setBackgroundColor(ContextCompat.getColor(context, R.color.visited));
                    board[row][col].setTextColor(ContextCompat.getColor(context,R.color.white));
                }
            });
        }

        private List<Button> findNextPath(int row , int col , Button[][] board){

            List<Button> list = new LinkedList<>();
//            Log.i(TAG, "Row: " + (row - 1));
            if(row - 1 >= 0 && !board[row - 1][col].getText().toString().equals("W")){
                list.add(board[row - 1][col]);
            }
//            Log.i(TAG, "Row: " + (row + 1));
            if (row + 1 < board.length && !board[row + 1][col].getText().toString().equals("W")){
                list.add(board[row + 1][col]);
            }
//            Log.i(TAG, "Col: " + (col - 1));
            if (col - 1 >= 0 && !board[row][col - 1].getText().toString().equals("W")){
                list.add(board[row][col - 1]);
            }
//            Log.i(TAG, "Col: " + (col + 1));
            if (col + 1 < board[row].length && !board[row][col + 1].getText().toString().equals("W")){
                list.add(board[row][col + 1]);
            }

            return list;
        }

    }

}
