public class EvaulationFunctionEmptySpaces implements IEvaluationFunction {
    @Override
    public int evalueStatus(Board board, char player, int turn) {
        char opponent;
        if (turn == 1) {
            player = 'o';
            opponent = 'x';
        }
        else {
            player = 'x';
            opponent = 'o';
        }

        int scores = 0;

        if(board.checkIfWin(player, board))
            scores += 1000;
        if (board.checkIfWin(opponent, board))
            scores += -1000;

        //count the same sign in col and compare with empty spaces in col
        for(int i=0; i<Board.BOARD_COLUMNS; i++){
            int lastMove = board.lastMoveInCol(i);
            if(lastMove < 5 && lastMove != -1){
                int empties = 5 - lastMove;
                int count = 0;
                for(int y=lastMove; y>=0; y--) {
                    if(board.fields[y][i] == player)
                        count ++;
                    else
                        break;
                }
                if((4-count) == empties)
                    scores += 21;
                else if((4-count) < empties)
                    scores += 32;
            }
        }

        //check is the same sign in row on left and on right 'x x ' 'x xx'
        for(int i=1; i<Board.BOARD_COLUMNS-2; i++){
            int firstEmpty = board.firstEmptyInCol(i);
            if(firstEmpty != -1){
                if(board.fields[firstEmpty][i-1] == player && board.fields[firstEmpty][i+1] == player
                    && (board.fields[firstEmpty][i+2] == player || board.fields[firstEmpty][i+2] == ' '))
                    scores += 45;
            }
        }

        //check is the same sign in row on left and on right ' x x' 'xx x'
        for(int i=2; i<Board.BOARD_COLUMNS-1; i++){
            int firstEmpty = board.firstEmptyInCol(i);
            if(firstEmpty != -1){
                if(board.fields[firstEmpty][i-1] == player && board.fields[firstEmpty][i+1] == player
                        && (board.fields[firstEmpty][i-2] == player || board.fields[firstEmpty][i-2] == ' '))
                    scores += 45;
            }
        }

        //check is 2 signs on right and 1 empty field ' xx '
        for(int i=0; i<Board.BOARD_COLUMNS-3; i++){
            int firstEmpty = board.firstEmptyInCol(i);
            if(firstEmpty != -1){
                if(board.fields[firstEmpty][i+1] == player && board.fields[firstEmpty][i+2] == player
                        && board.fields[firstEmpty][i+3] == ' ')
                    scores += 32;
            }
        }

        //check is 3 signs on right and 1 empty field ' xxx '
        for(int i=0; i<Board.BOARD_COLUMNS-4; i++){
            int firstEmpty = board.firstEmptyInCol(i);
            if(firstEmpty != -1){
                if(board.fields[firstEmpty][i+1] == player && board.fields[firstEmpty][i+2] == player
                        && board.fields[firstEmpty][i+3] == player && board.fields[firstEmpty][i+4] == ' ')
                    scores += 100;
            }
        }


        return scores;
    }
    @Override
    public String toString() {
        return "EvaulationFunctionEmptySpaces";
    }
}
