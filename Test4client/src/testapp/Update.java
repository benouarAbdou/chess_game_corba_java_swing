/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import chesslogic.ChessGame;
import static java.lang.Thread.sleep;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import testapp.Client;
import static testapp.Client.obj;
import testapp.Test1;
import testapp.Test1Helper;

/**
 *
 * @author ZAHRA
 */
public class Update extends Thread{// sending and ack like msg to confirme that the player is still in the game
    public static Test1 obj;
    static int id;
    static boolean result, exit;
    public Update (int id){
        this.id=id;
        exit=false;
    }
         
        public void run(){
        try {
            System.out.println("hello launched");
            ORB orb = ORB.init(new String[0], null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            obj = (Test1) Test1Helper.narrow(ncRef.resolve_str("ABC"));
            while(!exit){
                result=obj.update(id);
                if(result==false){
                    System.out.println("the update is stopping the gae now");
                    ChessGame.endGame(0);
                }
                    sleep(800);//get sent after every 800ms

            }
        } catch (InvalidName ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFound ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotProceed ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static int timeStamp(){ //function that returns time in an int
    LocalTime myObj = LocalTime.now();
    String s=myObj.toString().replace(":","").replace(".", "");
    return Integer.valueOf(s);
    }
        
        
    public void stopp(){//too stop running the thread
        exit=true;
        System.out.println("hello stopped");
    }
}
