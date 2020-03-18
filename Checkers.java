import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Date;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Checkers {
    private void showMenu() {
        String options[] = { "Developer(s)", "Instructions on how to play", "Play", "Show Winners", "Exit" }; // By
                                                                                                              // using
                                                                                                              // this,
                                                                                                              // options
                                                                                                              // can be
                                                                                                              // changes
                                                                                                              // easier
                                                                                                              // if
                                                                                                              // needed
        for (int i = 0; i < options.length; i++)
            System.out.println("Press " + (i + 1) + " for " + options[i]);
    }

    private void printInstructions() {
        System.out.println("\nINSTRUCTIONS\n");
        System.out.println(
                "The goal of Checkers, or \"Draughts\", is to remove all your opponent's pieces from \n the board. Use coordinates (row and column) to move your \n pieces around the board. Your pieces can only move forward one tile diagonally.\n");
        System.out.println(
                "To capture an opponent's piece and remove it from the board, you need to \"jump\" \n over their piece with one of yours. Jumping is mandatory, which means you have to jump \n with one of your pieces if you are able to.\n ");
        System.out.println(
                "If one of your pieces gets to the opposite side of the board (your opponent's back \n row), it will turn into a King. Kings can move and jump diagonally in any direction \n (remember, your regular pieces can only move forward). Kings can even combine jumps \n forward and backward on the same turn!\n");
        System.out.println(
                "You win by removing all your opponent's pieces from the board, or if your opponent \n can't make a move. “ - Reference site: https://www.coolmathgames.com/0-checkers\n");
    }

    private void optionListener() {
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        sc.close();
        switch (choice) {
            case 1:
                System.out.println("This checkers game is developed by: Shobhit Tewari");
                break;
            case 2:
                printInstructions();
                break;
            case 3:
                play();
                break;
            case 4:
                showWinners();
                break;
            case 5:
                System.out.println("Thank you. Game is closing.");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                } finally {
                    System.exit(0);
                }
                break;
            default:
                System.out.println("Choosing any other menu will lead to closing of the application");
                System.out.println("Thank you. Game is closing.");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                } finally {
                    System.exit(0);
                }
        }
    }

    private void showWinners() {
        File file = new File("gameResults.txt");
        if (!file.exists()) {
            System.out.println("Sorry no record till now");
            return;
        }
        String game = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((game = bufferedReader.readLine()) != null) {
                String date = game.substring(0, game.indexOf('–') - 1);
                String player1 = game.substring(game.indexOf('–') + 2, game.indexOf('(') - 1);
                String player2 = game.substring(game.indexOf(')') + 4, game.indexOf((char) 32, game.indexOf(')') + 5));
                String time = game.substring(game.indexOf('–', game.indexOf('–') + 1) + 2, game.length());
                System.out.println("Date: " + date);
                System.out.println("Player 1: " + player1 + " -->Winner<--");
                System.out.println("Player 2:" + player2);
                System.out.println("Time: " + time);
                System.out.println('\n');
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Some reading problem in file.");
            e.printStackTrace();
        }
    }

    private String getDate() {
        Date completeDate = new Date();
        String completeDateString = completeDate.toString();
        String date = "";
        boolean spaceCount = false;
        for (int i = completeDateString.indexOf((char) 32) + 1; i < completeDateString.length(); i++) {
            if (completeDateString.charAt(i) == (char) 32)
                if (spaceCount)
                    break;
                else
                    spaceCount = true;
            date = date + completeDateString.charAt(i);
        }
        date = date + ",";
        String year = completeDateString.substring(completeDateString.length() - 5);
        date = date + year;
        return date;
    }

    private String getTime(){
        DateFormat dateFromat = new SimpleDateFormat("dd/MM/yy HH:mm.ss");
        Date dateobj = new Date();
        String time = dateFromat.format(dateobj).toString();
        time = time.substring(time.indexOf(' ')+1, time.indexOf('.'));
        return time;
    }
    private void writeDataToFile(String player1, String player2, String startTime, String endTime, String date){ //Assumed player 1 always wins
        try{
            /*File file = new File("gameResults.txt");
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);*/
            FileWriter fWriter = new FileWriter("gameResults.txt", true);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            PrintWriter printWriter = new PrintWriter(bWriter);
            printWriter.println(date+" – "+player1 + " (won)"+ " vs "+player2+" – "+startTime+" to "+endTime);
            printWriter.close();
        }
        catch(Exception e){
            System.out.println("Data cannot be saved");
            e.printStackTrace();
        }
    }
    private void play(){

    }
    public static void main(String args[]){
        Checkers checkers = new Checkers();
        checkers.showMenu();
        checkers.optionListener();
    }
}
