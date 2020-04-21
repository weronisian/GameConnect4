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

    public boolean isEmptyField(){
        for(int i = 0; i< fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                if(fields[i][j] == ' ')
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



//    private Pair<Character, char[]>[] board;
//
//    public Board2(){
//        this.board = new Pair[7];
//        for(int i=0; i<board.length; i++){
//            char[] values = {' ', ' ', ' ', ' ', ' ', ' '};
//            board[i] = new Pair<>('a', values);
//        }
//    }

}
