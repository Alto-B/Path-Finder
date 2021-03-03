package com.comp2601.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private SolverThread solverThread;

    private TableLayout board;
    private Button solveBtn;
    private Button[][] allBoardBtn;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = findViewById(R.id.board);
        solveBtn = findViewById(R.id.solve_btn);

        solverThread = new SolverThread("Solver");
        game = new Game();
        allBoardBtn = new Button[Game.ROW_SIZE][Game.COL_SIZE];
        createBoard();

        solverThread.start();

        solveBtn.setOnClickListener((View v) -> {
            if(!game.isPlacedEnd() || !game.isPlacedStart()){
                Toast.makeText(this, "Please enter the start or destination", Toast.LENGTH_LONG).show();
                return;
            }

            if(!game.isDone()){
                disableAllBtn();
                solverThread.getHandler().post(new SolverThread.SolveBoard(game, this, solveBtn));
            }else{
                resetGame();
            }
        });
    }

    private void resetGame(){
        for(int i = 0; i < Game.ROW_SIZE; i++){
            for(int j = 0; j < Game.COL_SIZE; j++){
                allBoardBtn[i][j].setEnabled(true);
                allBoardBtn[i][j].setText("E");
                allBoardBtn[i][j].setTextColor(ContextCompat.getColor(this,R.color.black));
                allBoardBtn[i][j].setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }
        }

        solveBtn.setText("Solve");

        game.setPlacedStart(null);
        game.setPlacedEnd(null);
        game.setDone(false);
    }


    private void disableAllBtn(){
        for(int i = 0; i < Game.ROW_SIZE; i++){
            for(int j = 0; j < Game.COL_SIZE; j++){
                allBoardBtn[i][j].setEnabled(false);
                allBoardBtn[i][j].setTextColor(ContextCompat.getColor(this,R.color.black));
            }
        }
        solveBtn.setEnabled(false);
    }


    private void createBoard(){

        /*
        *  E = Empty position
        *  W = Wall position
        *  S = Starting position
        *  D = Destination position
        *  V = Visited position
        * */

        for (int i = 0; i < Game.ROW_SIZE; i++){
            TableRow tableRow = new TableRow(this);

            for (int j = 0; j < Game.COL_SIZE; j++){
                Button button = new Button(this);
                button.setText("E");
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                button.setTag("btn"+ i + "_" + j);

                button.setOnClickListener((View v) -> {
                    String currentPos = v.getTag().toString();
                    int rowPos = Integer.parseInt(currentPos.substring(3,currentPos.indexOf('_')));
                    int colPos = Integer.parseInt(currentPos.substring(currentPos.indexOf('_') + 1));

                    switch (game.getPosition(rowPos,colPos)){
                        case "E":
                            if(!game.isPlacedStart()){
                                button.setText("S");
                                button.setBackgroundColor(ContextCompat.getColor(this, R.color.start));
                                game.setPlacedStart(button);
                            }else if(!game.isPlacedEnd()){
                                button.setText("D");
                                button.setBackgroundColor(ContextCompat.getColor(this, R.color.end));
                                game.setPlacedEnd(button);
                            }else{
                                button.setText("W");
                                button.setBackgroundColor(ContextCompat.getColor(this, R.color.wall));
                            }
                            break;
                        case "S":
                            game.setPlacedStart(null);
                        case "D":
                            game.setPlacedEnd(null);
                        case "W":
                            button.setText("E");
                            button.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                            break;
                    }
                });

                game.setPosition(i,j,button);
                allBoardBtn[i][j] = button;
                tableRow.addView(button);
            }
            board.addView(tableRow);
        }
        board.setShrinkAllColumns(true);
    }

}
