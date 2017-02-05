package com.example.android.dartsscore;

/**
 * Custom class for saving the apps state, when selecting a value.
 * Made for the undo button click.
 */

public class ClickSave {
    // witch player's turn it is. If false, Player two's.
    public boolean playerOneTurn;
    // The value that was subtracted from a players score.
    public int subtracted;
    // How many shots the player had left before selecting a value.
    public int shotsLeft;
    public ClickSave(boolean playerOneTurn, int subtracted, int shotsLeft){
        this.playerOneTurn = playerOneTurn;
        this.subtracted = subtracted;
        this.shotsLeft = shotsLeft;
    }
}
