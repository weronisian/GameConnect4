public class EvaluationFunctionWinLose implements IEvaluationFunction {

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

//        //check is the same sign in column
//        for(int i=0; i<Board.BOARD_COLUMNS; i++){
//            int lastMove = board.lastMoveInCol(i);
//            if(lastMove != 5 && lastMove != -1){
//                if(board.fields[lastMove][i] == player)
//                    scores += 26;
//            }
//        }
//
//        //check is the same sign in row - right
//        for(int i=0; i<Board.BOARD_COLUMNS-1; i++){
//            int firstEmpty = board.firstEmptyInCol(i);
//            if(firstEmpty != -1){
//                if(board.fields[firstEmpty][i+1] == player)
//                    scores += 31;
//            }
//        }
//
//        //check is the same sign in row - left
//        for(int i=1; i<Board.BOARD_COLUMNS; i++){
//            int firstEmpty = board.firstEmptyInCol(i);
//            if(firstEmpty != -1){
//                if(board.fields[firstEmpty][i-1] == player)
//                    scores += 31;
//            }
//        }

//        System.out.println("Scores: "+scores);
        return scores;

    }

    @Override
    public String toString() {
        return "EvaluationFunctionWinLose";
    }
}
