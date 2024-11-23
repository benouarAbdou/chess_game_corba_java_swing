/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chesslogic;

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
public class Update extends Thread{
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
                int prev=timeStamp();
                result=obj.update(id);
                if(result==false){
                    System.out.println("the update is stopping the gae now");
                    ChessGame.endGame(0);
                }
                int time=timeStamp();
                if(time-prev<800){
                try {
                    sleep(800-time+prev);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChessGame.class.getName()).log(Level.SEVERE, null, ex);
                }
               }
            }
        } catch (InvalidName ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFound ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotProceed ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName ex) {
            Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static int timeStamp(){
    LocalTime myObj = LocalTime.now();
    String s=myObj.toString().replace(":","").replace(".", "");
    return Integer.valueOf(s);
    }
        
        
    public void stopp(){
        exit=true;
        System.out.println("hello stopped");
    }
}
