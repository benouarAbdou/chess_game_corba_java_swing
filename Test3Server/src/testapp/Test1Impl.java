/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;

/**
 *
 * @author ZAHRA
 */
public class Test1Impl extends Test1POA {
    private ORB orb;
    private int nbrPlayers = 0;
    private ArrayList<Room> matches = new ArrayList<Room>();
    private int temp;
    private int idcounter = 0;
    Semaphore sem = new Semaphore(0);

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    @Override
    public matchData matchMe() {
        System.out.println(".....................................");
        idcounter += 1;
        int id = idcounter;
        int k = 0;
        if (nbrPlayers == 0) {
            k = 1;
            try {
                nbrPlayers += 1;
                temp = id;
                sem.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Test1Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            matches.add(new Room(id, temp));
            sem.release();
            temp = 0;
            nbrPlayers = 0;
        }
        /*
         * for (Room m : matches){
         * System.out.println("id = "+m.playerCouples[0]+"  opponent = "+m.playerCouples
         * [1]);
         * 
         * }
         */
        return new matchData(k, id);

    }

    @Override
public data movePeice(data d) {
    Room ms = null;
    int opponent = 0;
        System.out.println("data received"+d.id+" played "+d.px+" "+d.py+" ------>"+d.mx+" "+d.my);
    try {
        // Find the match room
        for (Room m : matches) {
            boolean res = m.findMe(d.id);
            if (res) {
                ms = m;
                opponent = (m.playerCouples[0] == d.id) ? m.playerCouples[1] : m.playerCouples[0];
                break;
            }
        }

        if (ms == null) {
            System.out.println("No matching room found for player with id: " + d.id);
            return new data(9, 9, 9, 9, 9); // Return error if no match
        }

        if (d.px == -1) {
            // Get last move if it's the opponent's turn
            ms.update.acquire();
            System.out.println("data sent after "+ms.lastPiece[0]+" "+ms.lastPiece[1]+" "+ms.lastMove[0]+" "+ms.lastMove[1]);

            return new data( ms.lastPiece[0], ms.lastPiece[1],ms.lastMove[0], ms.lastMove[1], opponent);
        } else {
            // Update the last move and piece position
            ms.lastMove[0] = d.mx;
            ms.lastMove[1] = d.my;
            ms.lastPiece[0] = d.px;
            ms.lastPiece[1] = d.py;
            
            System.out.println("Player with id: " + d.id + " made a move.");

            // Release for the opponent to move
            ms.update.release();
            sleep(50);
            ms.update.acquire();
            // Don't wait here for opponent's move, just return immediately
            System.out.println("data sent after "+ms.lastPiece[0]+" "+ms.lastPiece[1]+" "+ms.lastMove[0]+" "+ms.lastMove[1]);
            return new data( ms.lastPiece[0], ms.lastPiece[1],ms.lastMove[0], ms.lastMove[1], opponent);
        }
    } catch (InterruptedException ex) {
        Logger.getLogger(Test1Impl.class.getName()).log(Level.SEVERE, null, ex);
    }

    return new data(9, 9, 9, 9, 9); // Return error data in case of failure
}


    @Override
    public void shutdown() {
        orb.shutdown(false);
    }

    public class Room {
        int[] playerCouples = new int[2];
        int[] lastPiece = new int[2];
        int[] lastMove = new int[2];
        Semaphore update = new Semaphore(0);

        public Room(int a, int b) {
            this.playerCouples[0] = a;
            this.playerCouples[1] = b;
        }

        public boolean findMe(int id) {
            if (this.playerCouples[0] == id || this.playerCouples[1] == id) {
                return true;
            } else {
                return false;
            }
        }
    }

}