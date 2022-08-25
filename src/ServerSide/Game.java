package ServerSide;

/**
 * Handles the logic of the game and determines if player can move to a selected grid space
 * Determines if a player has won or if a tie has occured
 */
public class Game {
    PlayerServer currentPlayer;
    PlayerServer[] grid = new PlayerServer[9];

    public void setCurrentPlayer(PlayerServer player){
        currentPlayer = player;
    }

    public void move(int locationGridValue, PlayerServer player) {
        if(player != currentPlayer){
            throw  new IllegalStateException("It's not your turn ");
        } else if (player.getOpponent() == null) {
            throw new IllegalStateException("You don't have an opponent yet");
        } else if (grid[locationGridValue] != null) {
            throw new IllegalStateException("This position is already take, try again");
        }
        grid[locationGridValue] = currentPlayer;
        currentPlayer = player.getOpponent();
    }

    public boolean hasPlayerWon(){
        return (grid[0] != null && grid[0] == grid[1] && grid[0] == grid[2])
                || (grid[3] != null && grid[3] == grid[4] && grid[3] == grid[5])
                || (grid[6] != null && grid[6] == grid[7] && grid[6] == grid[8])
                || (grid[0] != null && grid[0] == grid[3] && grid[0] == grid[6])
                || (grid[1] != null && grid[1] == grid[4] && grid[1] == grid[7])
                || (grid[2] != null && grid[2] == grid[5] && grid[2] == grid[8])
                || (grid[2] != null && grid[2] == grid[4] && grid[2] == grid[6])
                || (grid[0] != null && grid[0] == grid[4] && grid[0] == grid[8]);
    }

    //Checking if the gird is full
    public boolean hasTied(){
        for(PlayerServer player: grid){
            if (player == null){
                return false;
            }
        }
        return true;
    }
}
