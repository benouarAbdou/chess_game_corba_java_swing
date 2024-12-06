/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chesslogic;
import testapp.Update;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import jswing.Ending;
import testapp.Client;
import testapp.Client;
import testapp.data;

public class ChessGame {
    private static final Color DARK_BACKGROUND = new Color(45, 45, 45);
    private static final Color FIELD_BACKGROUND = new Color(60, 60, 60);
    private static final Color GREEN_BUTTON = new Color(92, 184, 92);
    private static final Color TEXT_COLOR = new Color(200, 200, 200);
    public static LinkedList<Piece> ps = new LinkedList<>();
    public static Piece bking;
    public static Piece wking;
    public static Piece selectedPiece ;
    public static boolean isMyTurn ;
    public static boolean isWhitePlayer;
    public static boolean isGameEnded;
    static JFrame frame;
    static JPanel pn;
    static Image imgs[] = new Image[12];
    public static int wt;
    static data d;
    static Update up;
    static Client client;
     static sendData sd;
     static String username;
     static int endit;
     
     public ChessGame(Client cl,String un)throws IOException, InterruptedException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
        username=un;
        client=cl;
         selectedPiece = null;
          wt=2;
         isMyTurn = client.isWhitePlayer;
         isWhitePlayer = client.isWhitePlayer;
         System.out.println("this rouns white ?"+isWhitePlayer);
         isGameEnded = false;
        up = new Update(client.id);
        endit=0;
        main(new String[0]);
     }
     
     
     
    public static void wtto1(Piece piece){//this is for the promotion frame (stoping the code till a promotion is done
        wt=1;
        System.out.println("\n\n\n\nwt changed\n\n\n\n");
        System.out.println("wtto1()");
        System.out.println("the data should be  x = "+piece.xp+"  x = "+piece.yp);
        System.out.println("piece is a "+piece.name);
        sd.start();
        sd=null;
    }
    
    public  void main(String[] args) throws IOException, InterruptedException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
        System.out.println("launching hello for the client . . . . .");
        up.setPriority(Thread.MAX_PRIORITY);
        up.start();//launching the update thread
        BufferedImage all = ImageIO
                .read(new File("src\\assets\\chess.png"));
        int ind = 0;
        for (int y = 0; y < 400; y += 200) {
            for (int x = 0; x < 1200; x += 200) {
                imgs[ind] = all.getSubimage(x, y, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
                ind++;
            }          
        }//this code takes the img of all pieces and seperate them to use
        ps.clear();
        bking = new Piece(4, 0, false, "king", ps);
        wking = new Piece(4, 7, true, "king", ps);

        //creating all pieces
        // Black pieces
        Piece brook = new Piece(0, 0, false, "rook", ps);
        Piece bknight = new Piece(1, 0, false, "knight", ps);
        Piece bbishop = new Piece(2, 0, false, "bishop", ps);
        Piece bqueen = new Piece(3, 0, false, "queen", ps);
        Piece bbishop2 = new Piece(5, 0, false, "bishop", ps);
        Piece bknight2 = new Piece(6, 0, false, "knight", ps);
        Piece brook2 = new Piece(7, 0, false, "rook", ps);
        for (int i = 0; i < 8; i++) {
            new Piece(i, 1, false, "pawn", ps);
        }

        // White pieces
        Piece wrook = new Piece(0, 7, true, "rook", ps);
        Piece wknight = new Piece(1, 7, true, "knight", ps);
        Piece wbishop = new Piece(2, 7, true, "bishop", ps);
        Piece wqueen = new Piece(3, 7, true, "queen", ps);
        Piece wbishop2 = new Piece(5, 7, true, "bishop", ps);
        Piece wknight2 = new Piece(6, 7, true, "knight", ps);
        Piece wrook2 = new Piece(7, 7, true, "rook", ps);
        for (int i = 0; i < 8; i++) {
            new Piece(i, 6, true, "pawn", ps);
        }

        frame = new JFrame();
        frame.setTitle(username);
        frame.setBounds(10, 10, 530, 552);
        //drawing the board and colors
        pn = new JPanel() {
            @Override
            public void paint(Graphics g) {
                boolean white = true;
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (white) {
                            g.setColor(new Color(179, 135, 100));
                        } else {
                            g.setColor(new Color(240, 216, 181));
                        }
                        /*
                         * if(y==7) {
                         * g.setColor(new Color(14, 8, 185));
                         * }
                         */
                        g.fillRect(x * 64, y * 64, 64, 64);
                        white = !white;
                    }
                    white = !white;
                }
                //drawing the valid moves 
                if (selectedPiece != null) {
                    LinkedList<int[]> moves = selectedPiece.getValidMoves(false);
                    g.setColor(new Color(255, 255, 0, 128)); // Highlight valid move squares in yellow
                    for (int[] move : moves) {
                        if (Math.abs(move[0] - selectedPiece.xp) == 2) { // Castling move
                            g.fillRect(move[0] * 64, move[1] * 64, 64, 64);
                        }
                        g.fillRect(move[0] * 64, move[1] * 64, 64, 64);
                    }
                }
                //drawing the imgs
                for (Piece p : ps) {
                    int ind = 0;
                    switch (p.name.toLowerCase()) {
                        case "king":
                            ind = 0;
                            break;
                        case "queen":
                            ind = 1;
                            break;
                        case "bishop":
                            ind = 2;
                            break;
                        case "knight":
                            ind = 3;
                            break;
                        case "rook":
                            ind = 4;
                            break;
                        case "pawn":
                            ind = 5;
                            break;
                    }
                    if (!p.isWhite)
                        ind += 6;
                    g.drawImage(imgs[ind], p.x, p.y, this);
                }
            }
        };
 

        frame.add(pn);
        pn.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {//moving the piece if dragged
                if (selectedPiece != null) {
                //System.out.println("5");
                    
                    selectedPiece.x = e.getX() - 32;
                    selectedPiece.y = e.getY() - 32;
                    frame.repaint();

                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        pn.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {//selecting a piece id selected
                System.out.println("11");
                if (isGameEnded)
                    return; // Prevent any further moves
                if (isMyTurn) {
                System.out.println("12");
                    
                    Piece p = getPiece(e.getX(), e.getY());
                    if (p != null && p.isWhite == isWhitePlayer) {
                        selectedPiece = p; // Only select a piece if it's the correct player's turn
                    }

                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
    //checking if the piece is in a valid move place . if yes the piece moves there
                
    if (isGameEnded) return; // Prevent any further moves if the game has ended

    if (selectedPiece != null) {
        // Try to move the selected piece to the new square
        int exp[]={selectedPiece.xp,selectedPiece.yp};//the old placement
        
        boolean moved = selectedPiece.move(e.getX() / 64, e.getY() / 64);//moving it
        frame.repaint();
        if (moved) {
            int r,temp=0;
            if (selectedPiece.name.equals("pawn")) {//checking promotion if it is a pawn 
                try {
                    temp = checkPromotion(selectedPiece);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChessGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
            // Send move data to the server (but don't block or wait for opponent)
            r=checkEndgame();//checking if checkmate
            
            // Switch turns after a valid move
            isMyTurn = !isMyTurn;
            if(r==0){
                r=checkEndgame();//checking if stalemate
            }
            // drawing for the new piece
            frame.repaint();

            
          
            if(r!=0){//the game is ended (stalemate/checkmate)
                endit=r;
            }
            if(r==3) r=1;//this is cuz we used the checkendgame var for the wining var 3 in checkmate means
            //the opponent lost

            if(r==0) r=temp; 
            Piece p=getPiece(selectedPiece.xp*64,selectedPiece.yp*64);
            //sending the data through corba
            sd =new sendData(new data(exp[0], exp[1], selectedPiece.xp*64, selectedPiece.yp*64, client.id,r));
            if(wt==2){
                //this is incase we called for promotion the code gives the frame the access to the process
                sd.start();
            }
            
        }

        selectedPiece = null;
    }
}


            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        if (client.isWhitePlayer==false){//firs initialization to satrt the game
            sendData sd=new sendData(new data(-1,-1,-1,-1,client.id,0));
            sd.start();
            frame.repaint();
        }
        
        
    }

    public static Piece getPiece(int x, int y) {
        int xp = x / 64;
        int yp = y / 64;
        for (Piece p : ps) {
            if (p.xp == xp && p.yp == yp) {
                return p;
            }
        }
        return null;
    }

    public static int checkEndgame() {
        // Find the current player's king
        Piece currentKing = null;
        for (Piece p : ps) {
            if (p.name.equalsIgnoreCase("king") && p.isWhite == isMyTurn) {
                currentKing = p;
                break;
            }
        }

        // Check if the current player's king is in check
        boolean isInCheck = isKingInCheck(currentKing);

        // Create a copy of the pieces list to iterate over safely
        LinkedList<Piece> psCopy = new LinkedList<>(ps);

        // Check if there are any valid moves left for the current player
        boolean hasValidMoves = false;
        for (Piece p : psCopy) {
            if (p.isWhite == isMyTurn) {
                LinkedList<int[]> moves = p.getValidMoves(false);
                for (int[] move : moves) {
                    // Simulate the move and check if the king is still in check
                    int oldXp = p.xp, oldYp = p.yp;
                    Piece capturedPiece = ChessGame.getPiece(move[0] * 64, move[1] * 64);
                    if (capturedPiece != null) {
                        capturedPiece.kill(); // Temporarily remove the piece for simulation
                    }
                    p.xp = move[0];
                    p.yp = move[1];

                    // Re-check if the king is still in check after this move
                    if (!isKingInCheck(currentKing)) {
                        System.out.println("move: x=" + move[0] + " y=" + move[1]);
                        hasValidMoves = true; // Valid move found
                    }

                    // Restore the board state after the simulation
                    p.xp = oldXp;
                    p.yp = oldYp;
                    if (capturedPiece != null) {
                        ps.add(capturedPiece); // Re-add the piece after simulation
                    }

                    if (hasValidMoves)
                        break;
                }
            }
            if (hasValidMoves)
                break;
        }

        // Determine the endgame condition
        if (!hasValidMoves) {
            System.out.println("no valid moves");
            if (isInCheck) {
                
                System.out.println("White is checkmated. Black wins!black is checkmated. White wins!");
                return 3;
            } else {
                System.out.println("Stalemate! The game is a draw.");
                return 2;
                // Handle stalemate
            }
        }
        System.out.println("no end game ");
        return 0;
    }

    public static boolean isKingInCheck(Piece king) {
        // Check if any opponent's piece can move to the king's position
        for (Piece p : ps) {
            if (p.isWhite != king.isWhite) { // Opponent's pieces only
                // Generate moves with the flag to avoid recursion
                LinkedList<int[]> opponentMoves = p.getValidMoves(true);
                for (int[] move : opponentMoves) {
                    if (move[0] == king.xp && move[1] == king.yp) {
                        return true; // King is in check
                    }
                }
            }
        }
        return false; // King is not in check
    }

    public static int checkPromotion(Piece piece) throws InterruptedException {
        int end = 7, py = (int) (64 * 3.5);
        if (piece.isWhite == true) {
            end = 0;
            py = 30;
        }
        if (piece.y / 60 == end) {
            System.out.println("here is a pawn getting promoted");
            wt=0;
            PFrame ppn = new PFrame(piece);
            System.out.println("before lock works");
           /* while(wt==0){
                sleep(50);
            }*/
            System.out.println("after lock works");
            String pname=piece.name;
 
        }
        return 0;
    }

    
    public static void changePiece(Piece piece, String newp) {
        piece.name = newp;
        frame.repaint();
    }

    public static void repaint(Graphics g) {
        boolean white = true;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (white) {
                    g.setColor(new Color(235, 235, 208));
                } else {
                    g.setColor(new Color(119, 148, 85));
                }
                g.fillRect(x * 64, y * 64, 64, 64);
                white = !white;
            }
            white = !white;
        }

        if (selectedPiece != null) {
            LinkedList<int[]> moves = selectedPiece.getValidMoves(false);
            g.setColor(new Color(255, 255, 0, 128)); // Highlight valid move squares in yellow
            for (int[] move : moves) {
                if (Math.abs(move[0] - selectedPiece.xp) == 2) { // Castling move
                    g.fillRect(move[0] * 64, move[1] * 64, 64, 64);
                }
                g.fillRect(move[0] * 64, move[1] * 64, 64, 64);
            }
        }

        for (Piece p : ps) {
            int ind = 0;
            switch (p.name.toLowerCase()) {
                case "king":
                    ind = 0;
                    break;
                case "queen":
                    ind = 1;
                    break;
                case "bishop":
                    ind = 2;
                    break;
                case "knight":
                    ind = 3;
                    break;
                case "rook":
                    ind = 4;
                    break;
                case "pawn":
                    ind = 5;
                    break;
            }
            if (!p.isWhite)
                ind += 6;
            g.drawImage(imgs[ind], p.x, p.y, pn);
        }

    }

    
    
    
    public static class sendData extends Thread {
        public static data dt;
     public sendData(data d){
         this.dt=d;
     }
     public void run(){
         
            if(dt.win==0 && wt==1){ //promotion over access this part
                wt=2;
                Piece tempp =getPiece(dt.mx,dt.my);
                String pname=tempp.name;
                if(pname=="queen"){
                    dt.win=5;
                }else if(pname=="bishop"){
                    dt.win=6;
                }else if(pname=="knight"){
                    dt.win=7;
                }else if(pname=="rook"){
                    dt.win=8;
                }
            }
         
         
            d=client.obj.movePeice(dt);//sending the data and waiting for the  next move
            if(endit!=0) {//ending the game if the last move was an ending move
                endGame(endit);
            }

            if(d.px==-1 && !isGameEnded){//making sure the game is ended correctly if the return is -1
                endGame(0);
            
            }else{
               if(!isGameEnded){//playing the move if not

            Piece opMove = getPiece(d.px*64,d.py*64);
            
            opMove.move(d.mx/64,d.my/64);
            //moving the piece and promoting if exists
            frame.repaint();
            if(d.win==5){
                changePiece(opMove,"queen");
            }else if(d.win==6){
                changePiece(opMove,"bishop");
            }else if(d.win==7){
                changePiece(opMove,"knight");
            }else if(d.win==8){
                changePiece(opMove,"rook");
            }
            else if( d.win==1){
                endGame(1);
            }else if( d.win==2){
                endGame(2);
            }

            frame.repaint();
                isMyTurn = !isMyTurn;//switching turns
            }
        }    
     }
}
    public static void endGame(int setting){//the function to end the game
        isGameEnded=true;
        up.stopp();
                String s="";
        if(setting==3){
            client.obj.updateUserStats(username, 1, 0, 0) ;
            s="congrats you won the game";
        }else if(setting==0){
            client.obj.updateUserStats(username, 1, 0, 0) ;
            s="your oponent left , you won the game";
        }else if(setting==1){
            client.obj.updateUserStats( username, 0, 0, 1) ;
            s="you lost this game";
        }else if(setting==2){
            client.obj.updateUserStats( username, 0, 1, 0) ;
            s="Stalemate! The game is a draw";
        }

        UIManager.put("OptionPane.background", DARK_BACKGROUND);
        UIManager.put("Panel.background", DARK_BACKGROUND);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        JOptionPane.showMessageDialog(frame, s, "game over", JOptionPane.ERROR_MESSAGE);
        new jswing.MainMenuPage(client, username).setVisible(true);
        frame.dispose(); 
    }

}