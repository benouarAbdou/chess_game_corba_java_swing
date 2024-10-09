package testapp;

import chesslogic.ChessGame;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import javax.swing.SwingUtilities;
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
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            obj = (Test1) Test1Helper.narrow(ncRef.resolve_str("ABC"));
            System.out.println("Welcome to chess game . wait to be paired with a player");
            matchData res = obj.matchMe();
            System.out.println("u have been paired . your id = " + res.id);

            // Launch the chess game interface
            launchChessGame();

            isWhitePlayer = res.k == 0;
            System.err.println("is white player  : " + isWhitePlayer);

            id = res.id;
            data result = new data();
            if (res.k == 0) {
                System.out.println("it's your opponent turn to play");
                result = obj.movePeice(new data(-1, -1, -1, -1, id));
                System.out.println("result === " + result.mx + "   " + result.px);
            }

            
        } catch (Exception e) {
            System.out.println("Hello Client exception: " + e);
            e.printStackTrace();
        }
    }

    

    private static void launchChessGame() {
        System.out.println("Attempting to launch chess game...");
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Initializing ChessGame...");
                ChessGame.main(new String[0]);
                System.out.println("ChessGame initialized successfully.");
            } catch (Exception e) {
                System.out.println("Error launching chess game: " + e);
                e.printStackTrace();
            }
        });
    }

}
