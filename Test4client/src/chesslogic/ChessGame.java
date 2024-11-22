/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chesslogic;
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
import javax.swing.JPanel;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import jswing.Ending;
import testapp.Client;
import testapp.Client;
import testapp.data;

public class ChessGame {
    public static LinkedList<Piece> ps = new LinkedList<>();
    public static Piece bking = new Piece(4, 0, false, "king", ps);
    public static Piece wking = new Piece(4, 7, true, "king", ps);
    public static Piece selectedPiece = null;
    public static boolean isMyTurn = Client.isWhitePlayer;
    public static boolean isWhitePlayer = Client.isWhitePlayer;
    public static boolean isGameEnded = false;
    static JFrame frame;
    static JPanel pn;
    static Image imgs[] = new Image[12];
    public static int wt=2;
    static data d;
    static Update up;
    static Client client;
     static sendData sd;
     static String username;
    public static void wtto1(Piece piece){
        wt=1;
        System.out.println("\n\n\n\nwt changed\n\n\n\n");
        System.out.println("wtto1()");
        System.out.println("the data should be  x = "+piece.xp+"  x = "+piece.yp);
        System.out.println("piece is a "+piece.name);
        sd.start();
        sd=null;
        
    }
    
    public static void main(String[] args,Client cl,String un) throws IOException, InterruptedException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
        username=un;
        client=cl;
        System.out.println("launching hello for the client . . . . .");
        up = new Update(client.id);
        up.setPriority(Thread.MAX_PRIORITY);
        up.start();
        BufferedImage all = ImageIO
                .read(new File("src\\assets\\chess.png"));
        int ind = 0;
        for (int y = 0; y < 400; y += 200) {
            for (int x = 0; x < 1200; x += 200) {
                imgs[ind] = all.getSubimage(x, y, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
                ind++;
            }
        }
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
        frame.setBounds(10, 10, 530, 552);
        pn = new JPanel() {
            @Override
            public void paint(Graphics g) {
                boolean white = true;
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (white) {
                            g.setColor(new Color(235, 235, 208));
                        } else {
                            g.setColor(new Color(119, 148, 85));
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
                    g.drawImage(imgs[ind], p.x, p.y, this);
                }
            }
        };
 

        frame.add(pn);
        pn.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
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
            public void mousePressed(MouseEvent e) {
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
                
    if (isGameEnded) return; // Prevent any further moves if the game has ended

    if (selectedPiece != null) {
        // Try to move the selected piece to the new square
        int exp[]={selectedPiece.xp,selectedPiece.yp};
        
        boolean moved = selectedPiece.move(e.getX() / 64, e.getY() / 64);
        frame.repaint();
            System.out.println("moved = "+moved);
        if (moved) {
            System.out.println("6");
            int r,temp=0;
            if (selectedPiece.name.equals("pawn")) {
                try {
                    temp = checkPromotion(selectedPiece);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChessGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("==============================================================before  "+isMyTurn);
            // Switch turns after a valid move
            isMyTurn = !isMyTurn;
            //System.out.println("it turned into + "+isMyTurn);
            System.out.println("name = "+selectedPiece.name);
            // Immediately snap the piece visually to the new location
            frame.repaint();

            // Send move data to the server (but don't block or wait for opponent)
            System.out.println("1");
            r=checkEndgame();
            if(r!=0){
                System.out.println("ending game line 231");
                endGame(r);
            }
            if(r==3){
                r=1;
            }
            if(r==0){
                r=temp;
            }
            System.out.println("/////////////////////////////////////////////////////////");
            System.out.println("/////////////////////////////////////////////////////////");
            System.out.println("xp "+selectedPiece.xp+"yp "+selectedPiece.xp);
            Piece p=getPiece(selectedPiece.xp*64,selectedPiece.yp*64);
            System.out.println("get piece ---- xp "+p.xp+"yp "+p.xp);
            System.out.println("/////////////////////////////////////////////////////////");
            System.out.println("/////////////////////////////////////////////////////////");
            System.out.println("/////////////////////////////////////////////////////////");
            sd =new sendData(new data(exp[0], exp[1], selectedPiece.xp*64, selectedPiece.yp*64, client.id,r));
            System.out.println("2");
            if(wt==2){
                sd.start();
            }
            //System.out.println("data has been transfered");
            // Check if the game has ended
            
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
        
        if (client.isWhitePlayer==false){
            sendData sd=new sendData(new data(-1,-1,-1,-1,client.id,0));
            sd.start();
            System.out.println("after thread");
            frame.repaint();
        }
        //update u=new update(Client.id);
        //u.start();
        
        
        
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
            if (isInCheck) {
                
                System.out.println(isMyTurn ? "White is checkmated. Black wins!" : "Black is checkmated. White wins!");
                return 3;
            } else {
                System.out.println("Stalemate! The game is a draw.");
                return 2;
                // Handle stalemate
            }
        }
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
         
            if(dt.win==0 && wt==1){
                System.out.println("-------------------------- we re in the change \n\n\n");
                wt=2;
                System.out.println("the data we r ending up with iss x = "+dt.mx/64+" y = "+dt.my/64);
                Piece tempp =getPiece(dt.mx,dt.my);
                System.out.println("name of the peice we re sending its change= "+tempp.name);
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
                System.out.println("win ih here ====== "+dt.win);
            }
         
         
            System.out.println("data sent "+dt.px+"  "+dt.py+" ----> "+dt.mx/64+" "+dt.my/64);
            d=client.obj.movePeice(dt);
            System.out.println("data returned ..........................................");
            System.out.println("data received "+d.px+"  "+d.py+" ----> "+d.mx/64+" "+d.my/64);
            if(d.px==-1 ){
                System.out.println("ending game line 500");
                endGame(0);
            
            }else{
            Piece opMove = getPiece(d.px*64,d.py*64);
            //System.out.println("data received "+d.px+"  "+d.py+" ----> "+d.mx/64+" "+d.my/64);
           // System.out.println("..................");
            System.out.println("piece moved"+opMove.name);
            
            opMove.move(d.mx/64,d.my/64);
                System.out.println("changing data (win var) = "+d.win);
            if(d.win==5){
                System.out.println("in 5");
                changePiece(opMove,"queen");
            }else if(d.win==6){
                System.out.println("in 6");
                changePiece(opMove,"bishop");
            }else if(d.win==7){
                System.out.println("in 7");
                changePiece(opMove,"knight");
            }else if(d.win==8){
                System.out.println("in 8");
                changePiece(opMove,"rook");
            }
            else if( d.win==1){
                endGame(1);
            }else if( d.win==2){
                endGame(2);
            }
           System.out.println("after changing the piece received = "+opMove.name);

            frame.repaint();
            System.out.println("...............................................................");
                isMyTurn = !isMyTurn;
               System.out.println("my turn now switched to "+isMyTurn);
            }
            
     }
}
    public static void endGame(int setting){
        System.out.println("game ended mode = "+setting);
        isGameEnded=true;
        up.stopp();
        System.out.println("update stopped");
        frame.dispose();
        new jswing.Ending(client,setting,username).setVisible(true);
        
    }

}