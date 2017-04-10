package com.example.android.dartsscore;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    // parameters for the numbers_layouts
    // ( I don't really understand why I need it, but it was the only solution i could find.)
    private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                        ViewGroup.LayoutParams.MATCH_PARENT,1);
    private boolean playerOneTurn = true;
    private int shotsLeft = 3;
    // The app saves values in it, witch will be used for the undo method.
    private ArrayList<ClickSave> listOfClicks = new ArrayList<>();
    private static final String STARTING_POINTS = "501";
    private TextView playerOne;
    private TextView playerTwo;
    private TextView countShots;
    private RadioGroup playersGroup;
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOne = (TextView) findViewById(R.id.player_one_score);
        playerTwo = (TextView) findViewById(R.id.player_two_score);
        countShots = (TextView) findViewById(R.id.shots_left);
        playersGroup = (RadioGroup) findViewById(R.id.players_group);
        parentLayout = (LinearLayout) findViewById(R.id.parent_layout);

        //create the layout for values 1 to 10
        LinearLayout firstColumn= (LinearLayout) findViewById(R.id.score_to_10);
        fillLayout(firstColumn, true, 0);
        //create the layout for values 11 to 20
        LinearLayout secondColumn= (LinearLayout) findViewById(R.id.score_to_20);
        fillLayout(secondColumn, false, 10);

        //assign onClickListener for the TextView of values 50 and 25
        TextView innerBull = (TextView) findViewById(R.id.inner_bull);
        makeTextClickable(innerBull, 50, false);
        TextView outerBull = (TextView) findViewById(R.id.outer_bull);
        makeTextClickable(outerBull, 25, false);

        //set the RadioGroup to player one.
        selectPlayerOne(true);

    }
    // For selecting a player with the radio buttons.
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.player_one_button:
                if (checked)
                    playerOneTurn = true;
                setShotsLeft(3);
                break;
            case R.id.player_two_button:
                if (checked)
                    playerOneTurn = false;
                setShotsLeft(3);
                break;
        }
    }
    // For reset button.
    public void reset(View view){

        playerOne.setText(STARTING_POINTS);

        playerTwo.setText(STARTING_POINTS);
        setShotsLeft(3);
        listOfClicks.clear();
        selectPlayerOne(true);
    }
    // For undo button. Sets the apps values to the ones saved the ArrayList's last item,
    // and deletes that item afterwards.
    public void undo(View view){
        if(listOfClicks.size() !=0){
            ClickSave thisClickSave = listOfClicks.get(listOfClicks.size()-1);
            if(thisClickSave.playerOneTurn){

                int beforeScore = Integer.parseInt(playerOne.getText().toString()) + thisClickSave.subtracted;
                String beforeScoreString = String.valueOf(beforeScore);
                playerOne.setText(beforeScoreString);
                selectPlayerOne(true);
            }
            else{
                int beforeScore = Integer.parseInt(playerTwo.getText().toString()) + thisClickSave.subtracted;
                String beforeScoreString = String.valueOf(beforeScore);
                playerTwo.setText(beforeScoreString);
                selectPlayerOne(false);
            }
            setShotsLeft(thisClickSave.shotsLeft);
            listOfClicks.remove(listOfClicks.size()-1);

        }
    }


    //The method used for creating the clickable TextViews of number values.
    private void fillLayout(LinearLayout layout, boolean whiteNext, int startCount){
        for(int i = 1; i< 11; i++){
            int count = i+startCount;
            LinearLayout row;

            LinearLayout line =(LinearLayout)getLayoutInflater().inflate(R.layout.simple_line, parentLayout, false);

            if(whiteNext){
                row = (LinearLayout)getLayoutInflater().inflate(R.layout.numbers_layout_white,parentLayout, false );

            }
            else{
                row = (LinearLayout)getLayoutInflater().inflate(R.layout.numbers_layout_black, parentLayout, false);

            }
            //set the layout parameters (Again, I don't know why i need it but the layouts don't show without it.)
            row.setLayoutParams(params);

            TextView singleText = (TextView) row.findViewById(R.id.text_single);
            String countString = String.valueOf(count);
            singleText.setText(countString);
            makeTextClickable(singleText, count, false);
            TextView doubleText = (TextView) row.findViewById(R.id.text_double);
            makeTextClickable(doubleText, count*2, true);
            TextView tripleText = (TextView) row.findViewById(R.id.text_triple);
            makeTextClickable(tripleText, count*3, false);

            layout.addView(row);
            layout.addView(line);
        }
    }

    //The method used for assigning an onClickListener to the TextViews.
    private void makeTextClickable(TextView text, final int value, final boolean checkIfDouble) {
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //modify score of player one
                if (playerOneTurn) {
                    if(shotsLeft == 0){
                        showToastMessage(R.string.out_of_shots_player_one);
                    }
                    else{
                        int newScore = Integer.parseInt(playerOne.getText().toString()) - value;
                        String newScoreString = String.valueOf(newScore);
                        if(newScore < 1){
                            //It only lets you reach 0 by scoring a double.
                            if(newScore == 0 && checkIfDouble){
                                playerOne.setText(newScoreString);
                                showToastMessage(R.string.toast_player_one_win);
                                listOfClicks.add(new ClickSave(playerOneTurn, value, shotsLeft));
                            }
                            else{
                                showToastMessage(R.string.toast_double_out);
                            }
                        }

                        else {
                            playerOne.setText(newScoreString);
                            listOfClicks.add(new ClickSave(playerOneTurn, value, shotsLeft));
                            setShotsLeft(shotsLeft-1);
                        }
                    }

                }
                //modify score of player two
                // ***the only difference is the findViewById, and the winning toast message***
                else {
                    if(shotsLeft == 0){
                        showToastMessage(R.string.out_of_shots_player_two);
                    }
                    else{

                        int newScore = Integer.parseInt(playerTwo.getText().toString()) - value;
                        String newScoreString = String.valueOf(newScore);
                        if(newScore < 1){
                            if(newScore == 0 && checkIfDouble){
                                playerTwo.setText(newScoreString);
                                showToastMessage(R.string.toast_player_two_win);
                                listOfClicks.add(new ClickSave(playerOneTurn, value, shotsLeft));
                            }
                            else{
                                showToastMessage(R.string.toast_double_out);
                            }
                        }

                        else {
                            playerTwo.setText(newScoreString);
                            listOfClicks.add(new ClickSave(playerOneTurn, value, shotsLeft));
                            setShotsLeft(shotsLeft-1);
                        }
                    }

                }
            }
        });
    }
    // Creates a toast message. It needs a strings.xml value.
    private void showToastMessage(int stringResource){
        Context context = getApplicationContext();
        CharSequence text = getResources().getString(stringResource);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    // Sets shotsLeft variable, and it's TextView.
    private void setShotsLeft(int i){
        shotsLeft = i;
        String shotsLeftString = String.valueOf(shotsLeft);
        countShots.setText(shotsLeftString);
    }
    // Modifies the radio group to player one(on true) or player two(on false).
    private void selectPlayerOne(boolean playerOne){
        if(playerOne){
            playersGroup.check(R.id.player_one_button);
            playerOneTurn = true;
        }
        else {
            playersGroup.check(R.id.player_two_button);
            playerOneTurn = false;
        }
    }

}
