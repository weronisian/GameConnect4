import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Results {

    public void writeAllParameters(int depth1, int depth2, boolean alfabeta1, boolean alfabeta2) {
        File plik = new File("ResultsMoveAndTime.csv");
        try {
            BufferedWriter record = new BufferedWriter(new FileWriter(plik, true));

            record.newLine();
            record.write(Integer.toString(depth1));
            record.write(";" + Integer.toString(depth2));
            record.write(";" + Boolean.toString(alfabeta1));
            record.write(";" + Boolean.toString(alfabeta2)+";");
            record.newLine();
            record.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeMoveAndTime(long time1, long time2, int moves1, int moves2) {
        File plik = new File("ResultsMoveAndTime.csv");
        try {
            BufferedWriter record = new BufferedWriter(new FileWriter(plik, true));

            record.write(Long.toString(time1));
            record.write(";" + Long.toString(time2));
            record.write(";" + Integer.toString(moves1));
            record.write(";" + Integer.toString(moves2)+";");
            record.newLine();
            record.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeAvgTime(int depth1, long time1, int moves1) {
        File plik = new File("ResultsAvgTimeForDepth.csv");
        try {
            BufferedWriter record = new BufferedWriter(new FileWriter(plik, true));

            record.write(Integer.toString(depth1));
            record.write(";" + Long.toString(time1));
            long avg = time1 / moves1;
            record.write(";" + Long.toString(avg));
            record.write(";" + Integer.toString(moves1)+";");
            record.newLine();
            record.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeDepthAndMovesToWin(boolean alfabeta1, boolean alfabeta2, int depth1, int depth2, long timeToWin,
                                        int movesToWin, int win) {
        File plik = new File("ResultsDepthAndMovesToWin.csv");
        try {
            BufferedWriter record = new BufferedWriter(new FileWriter(plik, true));

            record.write(Boolean.toString(alfabeta1));
            record.write(";" + Boolean.toString(alfabeta2));
            record.write(";" + Integer.toString(depth1));
            record.write(";" + Integer.toString(depth2));
            record.write(";" + Long.toString(timeToWin));
            long avg = timeToWin / movesToWin;
            record.write(";" + Long.toString(avg));
            record.write(";" + Integer.toString(movesToWin));
            record.write(";" + Integer.toString(win)+";");
            record.newLine();
            record.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
