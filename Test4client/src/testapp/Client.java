package testapp;

import chesslogic.ChessGame;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import javax.swing.SwingUtilities;
import jswing.LoginSignupPage;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class Client {
    static Scanner myObj = new Scanner(System.in);
    public static int id;
    public static Test1 obj;
    public static boolean isWhitePlayer;
    public static void main(String[] args) {
        try {
            System.out.println("testapp.Client.main()");
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            obj = Test1Helper.narrow(ncRef.resolve_str("ABC"));
            System.out.println("testapp.Client.main()");

            // Launch the login/signup page
            SwingUtilities.invokeLater(() -> {
            System.out.println("testapp.Client.main()");
                LoginSignupPage loginSignupPage = new LoginSignupPage(new Client());
                loginSignupPage.setVisible(true);
            });

        } catch (Exception e) {
            System.out.println("Client exception: " + e);
            e.printStackTrace();
        }
    }

}
