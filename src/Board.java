public class Board {
    public static int BOARD_ROWS = 6;
    public static int BOARD_COLUMNS = 7;
    public char[][] fields;

    public Board(){
        // new board 6 rows (1,2,3,4,5,6) x 7 columns (A,B,C,D,E,F,G)
        this.fields = new char[6][7];
        for(int i = 0; i< fields.length; i++){
            for (int j = 0; j< fields[0].length; j++) {
                fields[i][j] = ' ';
            }
        }
    }

    public Board(Board board){
        // new board 6 rows (1,2,3,4,5,6) x 7 columns (A,B,C,D,E,F,G)
        this.fields = new char[6][7];
        for(int i = 0; i< fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                fields[i][j] = board.fields[i][j];
            }
        }
    }

    public int firstEmptyInCol(int col){
        for(int i=0; i<fields.length; i++){
            if(fields[i][col] == ' ')
                return i;
        }
        return -1;
    }

    public int lastMoveInCol(int col){
        for(int i=0; i<fields.length; i++){
            if(fields[i][col] == ' ')
                return i-1;
        }
        return 5;
    }


    public boolean isEmptyField(){
        for(int i = 0; i< fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                if(fields[i][j] == ' ')
                    return true;
            }
        }
        return false;
    }

    public boolean checkIfWin(char lastPlayer, Board board){
        // horizontalCheck
        for (int i = 0; i<Board.BOARD_ROWS; i++){
            for (int j = 0; j<Board.BOARD_COLUMNS-3 ; j++ ){
                if (board.fields[i][j] == lastPlayer && board.fields[i][j+1] == lastPlayer
                        && board.fields[i][j+2] == lastPlayer && board.fields[i][j+3] == lastPlayer){
                    return true;
                }
            }
        }
        // verticalCheck
        for (int i = 0; i<Board.BOARD_ROWS-3 ; i++ ){
            for (int j = 0; j<Board.BOARD_COLUMNS; j++){
                if (board.fields[i][j] == lastPlayer && board.fields[i+1][j] == lastPlayer
                        && board.fields[i+2][j] == lastPlayer && board.fields[i+3][j] == lastPlayer){
                    return true;
                }
            }
        }

        // diagonally
        for (int i=3; i<Board.BOARD_ROWS; i++){
            for (int j=0; j<Board.BOARD_COLUMNS-3; j++){
                if (board.fields[i][j] == lastPlayer && board.fields[i-1][j+1] == lastPlayer
                        && board.fields[i-2][j+2] == lastPlayer && board.fields[i-3][j+3] == lastPlayer)
                    return true;
            }
        }
        for (int i=3; i< Board.BOARD_ROWS; i++){
            for (int j=3; j<Board.BOARD_COLUMNS; j++){
                if (board.fields[i][j] == lastPlayer && board.fields[i-1][j-1] == lastPlayer
                        && board.fields[i-2][j-2] == lastPlayer && board.fields[i-3][j-3] == lastPlayer)
                    return true;
            }
        }
        return false;
    }


    public void displayBoard(){
        System.out.println("  -------------");
        for(int i=5; i>=0; i--){
            System.out.print(i+1 + "|");
            for (int j = 0; j< fields[0].length; j++) {
                System.out.print(fields[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("  -------------");
        System.out.println("  0 1 2 3 4 5 6");
    }

}
