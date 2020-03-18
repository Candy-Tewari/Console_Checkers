import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Checkers{
    private void showMenu() {
        String[] options = { "Developer(s)", "Instructions on how to play", "Play", "Show Winners", "Exit" }; // By
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
                } catch (Exception ignored) {
                } finally {
                    System.exit(0);
                }
                break;
            default:
                System.out.println("Choosing any other menu will lead to closing of the application");
                System.out.println("Thank you. Game is closing.");
                try {
                    Thread.sleep(1000);
                } catch (Exception ignored) {
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
        StringBuilder date = new StringBuilder();
        boolean spaceCount = false;
        for (int i = completeDateString.indexOf((char) 32) + 1; i < completeDateString.length(); i++) {
            if (completeDateString.charAt(i) == (char) 32)
                if (spaceCount)
                    break;
                else
                    spaceCount = true;
            date.append(completeDateString.charAt(i));
        }
        date.append(",");
        String year = completeDateString.substring(completeDateString.length() - 5);
        date.append(year);
        return date.toString();
    }

    private String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm.ss");
        Date dateobj = new Date();
        String time = dateFormat.format(dateobj);
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
        System.out.println("You Are White");
        System.out.println("Enter the name of the player 2: ");
        String player2 = br.readLine();
        System.out.println("You are Black");
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
    public static void main(String[] args)throws IOException{
        Checkers checkers = new Checkers();
        checkers.showMenu();
        checkers.optionListener();
    }
}
class CheckerPlayGame{
    String[][]board = new String[8][8];
    ArrayList<Piece> allPieces=new ArrayList<>();
    int WhitePoints=0;
    int BlackPoints=0;
    public String[][] getBoard() {
        return board;
    }
    //used for creating a STANDARD board
    //puts the pieces at their initial positions and creates a new piece of its type
    private void setTheBoard(){
       //sets the black pieces
        for(int i=0;i<3;i++){
            for(int j=0;j<8;j++){
                if((i+j)%2!=0) {
                   allPieces.add(new Piece(PieceType.BLACK,(i*8+j)));
                    board[i][j] = "BP";
                }
                    else
                    board[i][j]="--";
            }
        }
       //sets the white pieces
        for(int i=5;i<8;i++){
            for(int j=0;j<8;j++){
                if((i+j)%2!=0) {
                    allPieces.add(new Piece(PieceType.WHITE,(i*8+j)));
                    board[i][j] = "WP";
                }
                else
                    board[i][j]="--";
            }
        }
        // sets the remaining positions
        for(int i=3;i<=4;i++)
            for(int j=0;j<8;j++)
                board[i][j]="--";
    }
//prints the complete board
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
        //this is the main function for playing the game
        // while loops until both players have even a single piece
        // At last return 0 if white has won and 1 if black has won

        setTheBoard();
        printBoard();

        boolean white_turn=true;//to find whose turn is
        Scanner sc=new Scanner(System.in);
        while(true){

            //This loop will conitnue working until and unless 1 player has won
            //Input be of the type -> "a2 to b3. From (file)(rank) to (file)(rank)"
            //Here all the logic of playing shall execute

           if(white_turn)
               System.out.println("White Turn");
           else
               System.out.println("Black Turn");
            System.out.println("Enter the Piece Which You Want to move");
            String x=sc.nextLine();//Takes a string in form of ColumnRow e.g A4
            char[] k=x.toCharArray();//Converts a string to character array

            int old_position=(k[0]-65)+(k[1]-49)*8;//Converts to the index value
            //Checking for validity
            if(old_position>63||old_position<0)
            {
                System.out.println("Wrong Value");
                continue;}
            System.out.println("Enter the new Position");

            String y=sc.nextLine();//Takes a string in form of ColumnRow e.g A4
            k=y.toCharArray();

            int new_position=(k[0]-65)+((int)(k[1])-49)*8;

            if(new_position>63||new_position<0)
            {
                System.out.println("Wrong Value");
                continue;
            }
            //This Iterates over the complete ArrayList of Pieces and finds which piece position refers to the old position
            // Then it Makes a move and gives turn to other if move is legal else give you again turn
            for (Piece p:allPieces)
            {
             //This checks piece type and whose turn is
                if((p.getType() == PieceType.BLACK) && !white_turn)
                {

                    if(p.getPosition()==old_position)
                    {
                        System.out.println(p.getLegalMoves(board));
                        if(!p.Move(new_position,this,allPieces))
                        {
                            System.out.println("Wrong Move");
                        }

                        else {
                            white_turn=true;
                        }
                    }
                }
                if((p.getType() == PieceType.WHITE) && white_turn)
                {
                    if(p.getPosition()==old_position)
                    {
                        System.out.println(p.toString());
                        System.out.println(p.getLegalMoves(board));
                        if(!p.Move(new_position,this,allPieces))
                        {
                            System.out.println("Wrong Move");
                        }
                        else
                        {
                            white_turn=false;
                        }
                    }
                }
            }

            printBoard();




            int white=0,black=0;
//Now we have to check if board has both types of pieces
// if any doesn't have then opposition wins
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {

                    if(board[i][j].equals("WP") || board[i][j].equals("WK"))
                    {
                        white++;
                    }
                        else if(board[i][j].equals("BP") || board[i][j].equals("BK"))
                    {
                        black++;
                    }
                }
//                used only for points calculating
                WhitePoints=12-white;
                BlackPoints=12-black;
            }
            System.out.println("White Points: "+WhitePoints);
            System.out.println("Black Points: "+BlackPoints);
            if(white==0)   return 1;//if white pieces are zero then returns 1 which indicates black has won
            if(black==0)   return 0;//if black pieces are zero then return 0 which indicates white has won
        }


    }
}
class Piece {

    PieceType type;  //type of piece black or white
     int position;//position(0-63)
    boolean king;//used for king if king is true then can move in reverse order
     public static final boolean[] FirstColumn=getValuesForColumn(0);//Used for First Column check
     public static final boolean[] LastColumn=getValuesForColumn(7);//Used for Last Column check
     public static final boolean[] FirstRow=getValuesForRow(0);//Used for First Row check
     public static final boolean[] LastRow=getValuesForRow(56);//Used for Last Row check
     public int[] LegalMoveType={7,-7,-9,9};//used for moves type
     public Piece(PieceType type, int position)
     {
         this.type = type;
         this.position = position;
         this.king=false;
 
     }
     //for making king true if reaches opposite side
     public void king_made()
     {
         king=true;
     }
 
     @Override
     public String toString() {
         if(type==PieceType.BLACK &&king)
         return "BK";
         if (type==PieceType.BLACK &&!king)
             return "BP";
         if(type==PieceType.WHITE&& king)
             return "WK";
         else
             return "WP";
     }
 //For making a move
     public boolean Move(int val, CheckerPlayGame board,List<Piece> allPieces)
     {
         //Checks if legal moves is possible
         if(!getLegalMoves(board.getBoard()).contains(val))
             return false;
 
         //puts present position as empty
         board.getBoard()[position/8][position%8]="--";
 
         //If piece is White and reaches in first row then piece becomes king
         if(FirstRow[val]&&(type==PieceType.WHITE)) {
             king_made();
             board.getBoard()[val/8][val%8]="BK";
         }
 
         //If piece is black and reaches in last row then piece becomes king
         if(LastRow[val]&&type==PieceType.BLACK)
         {
             king_made();
             board.getBoard()[val/8][val%8]="WK";
 
         }
 
         //for removing all pieces which are attacked
         while (val!=position)
         {
             //if val is greater than position only possible move are 7 and 9
             if(val>position)
             {
                 if((val-position)%7==0)
                 {
                     position+=7;
                     if(!board.getBoard()[position / 8][position % 8].equals("--"))
                     {
                         allPieces.removeIf(p -> p.getPosition() == position);
                         board.getBoard()[position/8][position%8]="--";
                     }
                 }
                 else if((val-position)%9==0)
                 {
                     position+=9;
                     if(!board.getBoard()[position / 8][position % 8].equals("--"))
                     {
                         allPieces.removeIf(p -> p.getPosition() == position);
                         board.getBoard()[position/8][position%8]="--";
                     }
                 }
             }
             //if val is smaller than position only possible move are -7 and -9
             else {
                 if((position-val)%7==0)
                 {
                     position-=7;
                     if(!board.getBoard()[position / 8][position % 8].equals("--"))
                     {
                         allPieces.removeIf(p -> p.getPosition() == position);
                         board.getBoard()[position/8][position%8]="--";
                     }
                 }
                 else if((position-val)%9==0)
                 {
                     position-=9;
                     if(!board.getBoard()[position / 8][position % 8].equals("--"))
                     {
                         allPieces.removeIf(p -> p.getPosition() == position);
                         board.getBoard()[position/8][position%8]="--";
                     }
                 }
 
             }
         }
 
          board.getBoard()[val/8][val%8]=this.toString();
 
         return true;
     }
 
 //calculates the legal moves of a piece
     public List<Integer> getLegalMoves(String[][] board){
 
        ArrayList<Integer> movesPossible=new ArrayList<>();
        for(int i=0;i<4;i++)
        {
 
            int value=LegalMoveType[i];
            int position_now=this.position;
               while(ConditionForValidity(position_now))
               {
                //used for first and last column exclusion
                   if (checkFirstColumnExclusion(position, (type.getMoveDir())*value) || checkLastColumnExclusion(position, (type.getMoveDir())*value)) {
 
                       break;
                   }
 
                   //these moves are possible only if the piece is king
                   if((value==-7||value==-9)&&!king)
                       break;
 
                   position_now += type.getMoveDir()*value;
 
                   if(ConditionForValidity(position_now))
                   {
 
                       //used for finding the presence of piece at that position
                       if(type==PieceType.BLACK)
                       {
                           //if same type of piece comes then move not legal and next positions can*'t be legal
 
                           if(board[position_now / 8][position_now % 8].equals("BP")
                                   || board[position_now / 8][position_now % 8].equals("BK"))
                            break;
 
                           //if opposite type of piece comes then move not legal but next positions are legal
                           if (board[position_now / 8][position_now % 8].equals("WP")
                                   || board[position_now / 8][position_now % 8].equals("WK"))
                               continue;
 
                       }
 
                       if ((type==PieceType.WHITE))
                       {
                           //if same type of piece comes then move not legal and next positions can*'t be legal
                           if (board[position_now / 8][position_now % 8].equals("WP") || board[position_now / 8][position_now % 8].equals("WK"))
                           {
                               break;
                           }
 
                           //if opposite type of piece comes then move not legal but next positions are legal
                           if(board[position_now / 8][position_now % 8].equals("BP") || board[position_now / 8][position_now % 8].equals("BK"))
                               continue;
 
 
                       }
                       movesPossible.add(position_now);
                   }
                   }
 
         }
 
         return movesPossible;
     }
 
     //checks the validity of that position i.e. position is in between 0-63
     private boolean ConditionForValidity(int position_now) {
         return position_now <= 63 && position_now >= 0;
     }
 
 
     //checks the first column exclusion i.e. if it is first column and then move type is 7 or -9
     private boolean checkFirstColumnExclusion(int currentPosition,int MoveType)
     {
         return (MoveType==7||MoveType==-9)&& FirstColumn[currentPosition];
     }
 
     //checks the last column exclusion i.e. if it is first column and then move type is -7 or 9
     private boolean checkLastColumnExclusion(int currentPosition,int MoveType)
     {
         return (MoveType==-7||MoveType==9)&& LastColumn[currentPosition];
     }
 
     public PieceType getType() {
         return type;
     }
 
     public int getPosition() {
         return position;
     }
 
     //gives the first and last column values as true
     // It used because these columns have limited moves
     private static boolean[] getValuesForColumn(int i)
     {
         boolean[] Column=new boolean[64];
         for(int j=i;j<64;j=j+8)
         {
             Column[j]=true;
 
         }
         return Column;
     }
 
     //gives the first and last row as true used
     // it is used for changing the piece type as king
     private static boolean[] getValuesForRow(int j)
     {
         boolean[] Column=new boolean[64];
         for (int i=0;i<8;i++)
         {
             Column[i+j]=true;
         }
 
         return Column;
     }
 
 
 }
enum PieceType {
    //    used for black and white values and given directions
        BLACK(1), WHITE(-1);
    
        final int moveDir;
        public int getMoveDir() {
            return moveDir;
        }
    
        PieceType(int moveDir) {
            this.moveDir = moveDir;
        }
    }
 