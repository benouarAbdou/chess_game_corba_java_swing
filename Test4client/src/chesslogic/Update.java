/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chesslogic;

import static java.lang.Thread.sleep;
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
    static boolean exit=false;
    public Update (int id){
        this.id=id;
    }
         
        public void run(){
        try {
            System.out.println("hello launched");
            ORB orb = ORB.init(new String[0], null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            obj = (Test1) Test1Helper.narrow(ncRef.resolve_str("ABC"));

            boolean result;
            while(!exit){
                result=obj.update(id);
                if(result==false){
                    ChessGame.endGame(0);
                }
                try {
                    sleep(400);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChessGame.class.getName()).log(Level.SEVERE, null, ex);
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
        
        
        
    public void stopp(){
        exit=true;
        System.out.println("hello stopped");
    }
}
