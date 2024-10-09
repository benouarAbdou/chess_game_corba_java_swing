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
        try {
            for (Room m : matches) {
                boolean res = m.findMe(d.id);
                if (res = true) {
                    ms = m;
                    if (m.playerCouples[0] == d.id) {
                        opponent = m.playerCouples[1];
                    } else {
                        opponent = m.playerCouples[0];
                    }
                    break;
                }
            }

            // System.out.println("---------------------"+ms.playerCouples[0]+"
            // "+ms.playerCouples[1]);

            if (d.mx == -1) {
                ms.update.acquire();
                // System.out.println(ms.lastMove[0]+" "+ms.lastMove[1]+" "+ms.lastPiece[0]+"
                // "+ms.lastPiece[1]+" "+opponent);
                return new data(ms.lastMove[0], ms.lastMove[1], ms.lastPiece[0], ms.lastPiece[1], opponent);
            } else {

                ms.lastMove[0] = d.mx;
                ms.lastMove[1] = d.my;
                ms.lastPiece[0] = d.px;
                ms.lastPiece[1] = d.py;
                ms.update.release();
                sleep(50);
                ms.update.acquire();
                // System.out.println(ms.lastMove[0]+" "+ms.lastMove[1]+" "+ms.lastPiece[0]+"
                // "+ms.lastPiece[1]+" "+opponent);

                return new data(ms.lastMove[0], ms.lastMove[1], ms.lastPiece[0], ms.lastPiece[1], opponent);

            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Test1Impl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new data(9, 9, 9, 9, 9);
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
