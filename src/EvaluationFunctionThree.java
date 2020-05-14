public class EvaluationFunctionThree implements IEvaluationFunction {
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


        //check if there are 3 in the column
        for(int i=0; i<Board.BOARD_COLUMNS; i++){
            int lastMove = board.lastMoveInCol(i);
            if(lastMove < 5 && lastMove >= 2){
                if(board.fields[lastMove][i] == player && board.fields[lastMove-1][i] == player
                    && board.fields[lastMove - 2][i] == player)
                    scores += 51;
            }
        }

        //check 3 in row - right ' xxx'
        for(int i=0; i<Board.BOARD_COLUMNS-3; i++){
            int firstEmpty = board.firstEmptyInCol(i);
            if(firstEmpty != -1){
                if(board.fields[firstEmpty][i+1] == player && board.fields[firstEmpty][i+2] == player
                        && board.fields[firstEmpty][i+3] == player)
                    scores += 62;
                if(board.fields[firstEmpty][i+1] == opponent && board.fields[firstEmpty][i+2] == opponent
                        && board.fields[firstEmpty][i+3] == opponent)
                    scores -= 70;
            }
        }

        //check 3 in row - left 'xxx '
        for(int i=3; i<Board.BOARD_COLUMNS; i++){
            int firstEmpty = board.firstEmptyInCol(i);
            if(firstEmpty != -1){
                if(board.fields[firstEmpty][i-1] == player && board.fields[firstEmpty][i-2] == player
                    && board.fields[firstEmpty][i-3] == player)
                    scores += 62;
                if(board.fields[firstEmpty][i-1] == opponent && board.fields[firstEmpty][i-2] == opponent
                        && board.fields[firstEmpty][i-3] == opponent)
                    scores -= 70;
            }
        }

        //check 3 in row with space 'x xx'
        for(int i=1; i<Board.BOARD_COLUMNS-2; i++){
            int firstEmpty = board.firstEmptyInCol(i);
            if(firstEmpty != -1){
                if(board.fields[firstEmpty][i-1] == player && board.fields[firstEmpty][i+1] == player
                        && board.fields[firstEmpty][i+2] == player)
                    scores += 69;
            }
        }

        //check 3 in row with space 'xx x'
        for(int i=2; i<Board.BOARD_COLUMNS-1; i++){
            int firstEmpty = board.firstEmptyInCol(i);
            if(firstEmpty != -1){
                if(board.fields[firstEmpty][i-2] == player && board.fields[firstEmpty][i-1] == player
                        && board.fields[firstEmpty][i+1] == player)
                    scores += 69;
            }
        }

        return scores;
    }

    @Override
    public String toString() {
        return "EvaluationFunctionThree";
    }
}
