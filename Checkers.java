import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Date;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Checkers {
    private void showMenu() {
        String options[] = { "Developer(s)", "Instructions on how to play", "Play", "Show Winners", "Exit" }; // By
                                                                                                              // using
                                                                                                              // this,
                                                                                                              // options
                                                                                                              // can be
                                                                                                              // changed
                                                                                                              // easier,
                                                                                                              // if
                                                                                                              // needed
        for (int i = 0; i < options.length; i++)
            System.out.println("Press " + (i + 1) + " for " + options[i]);
    }

    private void printInstructions() {
        System.out.println("INSTRUCTIONS\n");
        System.out.println(
                "The goal of Checkers, or \"Draughts\", is to remove all your opponent's pieces from \n the board. Use coordinates (row and column) to move your \n pieces around the board. Your pieces can only move forward one tile diagonally.\n");
        System.out.println(
                "To capture an opponent's piece and remove it from the board, you need to \"jump\" \n over their piece with one of yours. Jumping is mandatory, which means you have to jump \n with one of your pieces if you are able to.\n ");
        System.out.println(
                "If one of your pieces gets to the opposite side of the board (your opponent's back \n row), it will turn into a King. Kings can move and jump diagonally in any direction \n (remember, your regular pieces can only move forward). Kings can even combine jumps \n forward and backward on the same turn!\n");
        System.out.println(
                "You win by removing all your opponent's pieces from the board, or if your opponent \n can't make a move. “ - Reference site: https://www.coolmathgames.com/0-checkers");
    }

    private void optionListener()throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice = Integer.parseInt(br.readLine());
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
    private void play()throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Player 1 will start the game. Game ends when a player is out of moves.");
        System.out.println("Enter the name of the player 1: ");
        String player1 = br.readLine();
        System.out.println("Enter the name of the player 2: ");
        String player2 = br.readLine();
        CheckerPlayGame checkerPlayGame = new CheckerPlayGame();
        String startTime = getTime();
        int winner = checkerPlayGame.startTheGame();
        String endTime = getTime();
        String date = getDate();
        if(winner==0)
            writeDataToFile(player1, player2, startTime, endTime, date); //white has won which is the first player
        else
            writeDataToFile(player2, player1, startTime, endTime, date); //black has won which is the second player
    }
    public static void main(String args[])throws IOException{
        Checkers checkers = new Checkers();
        checkers.showMenu();
        checkers.optionListener();
    }
}
class CheckerPlayGame{
    String[][]board = new String[8][8];
    private void setTheBoard(){

        for(int i=0;i<3;i++){
            for(int j=0;j<8;j++){
                if((i+j)%2==0)
                    board[i][j]="BP";
                else
                    board[i][j]="--";
            }
        }

        for(int i=5;i<8;i++){
            for(int j=0;j<8;j++){
                if((i+j)%2!=0)
                    board[i][j]="WP";
                else
                    board[i][j]="--";
            }
        }

        for(int i=3;i<=5;i++)
            for(int j=0;j<8;j++)
                board[i][j]="--";
    }

    private void printBoard(){
        System.out.print("   ");
        for(int i=1;i<=33;i++)
            System.out.print("–");
        System.out.println();
        System.out.print("    ");
        
        for(int i=0;i<=21;i++){
            if(i%3==0){
                String value=""+(char)(65+(i/3))+" ";
                System.out.print(value);
            }
            else{
                System.out.print(" ");
            }
        }
        System.out.println();
        System.out.print("   ");
        for(int i=1;i<=33;i++)
            System.out.print("–");
        System.out.println();
        System.out.print("|1| ");
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++)
                System.out.print(board[i][j]+"  ");
            System.out.print("|"+(i+1)+"|");
            System.out.println();
            if(i!=7)
                System.out.print("|"+(i+2)+"| ");
        }
        System.out.print("   ");
        for(int i=1;i<=33;i++)
            System.out.print("–");
        System.out.println();
        System.out.print("    ");
        
        for(int i=0;i<=21;i++){
            if(i%3==0){
                String value=""+(char)(65+(i/3))+" ";
                System.out.print(value);
            }
            else{
                System.out.print(" ");
            }
        }
        System.out.println();
        System.out.print("   ");
        for(int i=1;i<=33;i++)
            System.out.print("–");
        System.out.println();
    }
    public int startTheGame(){
        setTheBoard();
        printBoard();
       while(true){
           //This loop will conitnue working until and unless 1 player has won
           //Input be of the type -> "a2 to b3. From (file)(rank) to (file)(rank)"
           //Here all the logic of playing shall execute
       }
       //Just return 0 if white has won and 1 if black has won
       return 0; //0 indicates that white has won and 1 indicates that black has won
    }
}