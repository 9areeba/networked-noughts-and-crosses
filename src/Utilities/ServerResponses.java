package Utilities;

/**
 * Commands used by server player
 * playerController receives these commands and deals with them accordingly
 */
public enum ServerResponses {
    VALID_MOVE,
    OPPONENT_MOVED,
    STATUS,
    WIN,
    LOSE,
    TIE,
    PLAYER_LEFT,
    QUIT
}
