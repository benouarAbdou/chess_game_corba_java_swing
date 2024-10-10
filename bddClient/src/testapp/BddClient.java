package testapp;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import jswing.LoginSignupPage;

public class BddClient {
    public static Test1 obj;

    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            obj = Test1Helper.narrow(ncRef.resolve_str("ABC"));

            // Launch the login/signup page
            SwingUtilities.invokeLater(() -> {
                LoginSignupPage loginSignupPage = new LoginSignupPage(new BddClient());
                loginSignupPage.setVisible(true);
            });

        } catch (Exception e) {
            System.out.println("Client exception: " + e);
            e.printStackTrace();
        }
    }

    public boolean login(String username, String password) {
        return obj.login(username, password);
    }

    public boolean signup(String username, String password) {
        return obj.signup(username, password);
    }
 
    public String getUserStats(String username) {
        return obj.getUserStats(username);
    }

    public void updateUserStats(String username, int wins, int draws, int losses) {
        obj.updateUserStats(username, wins, draws, losses);
    }
     
}
