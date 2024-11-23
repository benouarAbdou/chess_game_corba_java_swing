/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;

/**
 *
 * @author ZAHRA
 */
public class Test1Impl extends Test1POA {
    public static int ServerTimer=0;
    private ORB orb;
    private int nbrPlayers = 0;
    private ArrayList<Room> matches = new ArrayList<Room>();
    private static ArrayList<cnxArray> cnx=new ArrayList<cnxArray>();
    private int temp;
    private int idcounter = 0;
    private cnxCheck cc=new cnxCheck();
    Semaphore sem = new Semaphore(0);
    private final String CSV_FILE = "users.csv";
    private final Map<String, UserStats> userDB = new HashMap<>();
    private int currentUserId = 0; // Counter for user IDs

    public void setORB(ORB orb_val) {
        orb = orb_val;
        cc.start();
        loadUsersFromFile();
    }

    @Override
    public matchData matchMe() {
        System.out.println(".....................................");
        idcounter += 1;
        int id = idcounter;
        int k = 0;
        if (nbrPlayers == 0) {
            System.out.println("client with id = "+id+" wants to start a match it is the white");
            k = 1;
            try {
                nbrPlayers += 1;
                temp = id;
                sem.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Test1Impl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("client with id = "+id+" wants to start a match it is the black");
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
        cnx.add(new cnxArray(id));
        System.out.println("********************************cnx array has *********************************");
        for(int i=0;i<cnx.size();i++){
            System.out.println("array : id = "+cnx.get(i).id + " .. "+cnx.get(i).lastAck);
        }
        System.out.println("*******************************************************************************");
        System.out.println("id = "+id+" k = "+k);
        return new matchData(k, id);

    }

    @Override
public data movePeice(data d) {
       // System.out.println("hi-------------------------------------------------------------------");
    Room ms = null;
    int opponent = 0;
        System.out.println("data received"+d.id+" played "+d.px+" "+d.py+" ------>"+d.mx+" "+d.my);
    try {
        
        ms = search(d.id);
        if (ms == null) {
            System.out.println("No matching room found for player with id: " + d.id);
            return new data(-1, -1,-1,-1,-1,0); // Return error if no match
        }
        System.out.println("room is "+ms.playerCouples[0]+" "+ms.playerCouples[1]);

        opponent = (ms.playerCouples[0] == d.id) ? ms.playerCouples[1] : ms.playerCouples[0];
        if (d.px == -1) {
            // Get last move if it's the opponent's turn
            ms.update.acquire();
            if(ms.lastPiece[0]==-1){
                ms.update.release();
            }
            System.out.println("data sent after "+ms.lastPiece[0]+" "+ms.lastPiece[1]+" "+ms.lastMove[0]+" "+ms.lastMove[1]);
            return new data( ms.lastPiece[0], ms.lastPiece[1],ms.lastMove[0], ms.lastMove[1], opponent,ms.win);
        } else {
            // Update the last move and piece position
            ms.lastMove[0] = d.mx;
            ms.lastMove[1] = d.my;
            ms.lastPiece[0] = d.px;
            ms.lastPiece[1] = d.py;
            ms.win=d.win;
            //System.out.println("Player with id: " + d.id + " made a move.");

            // Release for the opponent to move
            ms.update.release();
            sleep(50);
            if(d.win!=0 &d.win!=5 &d.win!=6 &d.win!=7 &d.win!=8 ){
                return new data( -1,-1,-1,-1,-1,-1);
            }
            ms.update.acquire();
            if(ms.lastPiece[0]==-1){
                ms.update.release();
            }
            // Don't wait here for opponent's move, just return immediately
            //System.out.println("data sent after "+ms.lastPiece[0]+" "+ms.lastPiece[1]+" "+ms.lastMove[0]+" "+ms.lastMove[1]);
            System.out.println("-------------------------------------------------win returned = "+ms.win);
            return new data( ms.lastPiece[0], ms.lastPiece[1],ms.lastMove[0], ms.lastMove[1], opponent,ms.win);
        }
    } catch (InterruptedException ex) {
        Logger.getLogger(Test1Impl.class.getName()).log(Level.SEVERE, null, ex);
    }

    return new data(9, 9, 9, 9, 9,0); // Return error data in case of failure
}


    // Method to handle user signup
    @Override
    public boolean signup(String username, String password) {
        if (userDB.containsKey(username)) {
            return false; // User already exists
        }

        // Assign a unique ID and create the new user with initial stats
        UserStats newUser = new UserStats(currentUserId++, username, password, 0, 0, 0);
        userDB.put(username, newUser);
        saveUserToFile(newUser); // Save user with initial stats and ID
        return true;
    }

    // Method to handle user login
    @Override
    public boolean login(String username, String password) {
        UserStats userStats = userDB.get(username);
        return userStats != null && password.equals(userStats.password);
    }

    // Method to save a new user to the CSV file
    private void saveUserToFile(UserStats user) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE, true))) {
            pw.println(user.id + "," + user.username + "," + user.password + "," + user.wins + "," + user.draws + "," + user.losses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load users from the CSV file into the userDB map
    private void loadUsersFromFile() {
        File file = new File(CSV_FILE);
    if (!file.exists()) return;  // If file doesn't exist, nothing to load
    try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
        String line;
        int maxId = 0;  // Track the highest user ID
        
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length == 6) { // Check for 6 fields: id, username, password, wins, draws, losses
                int id = Integer.parseInt(data[0]);
                String username = data[1];
                String password = data[2];
                int wins = Integer.parseInt(data[3]);
                int draws = Integer.parseInt(data[4]);
                int losses = Integer.parseInt(data[5]);
                // Put the user into the userDB
                userDB.put(username, new UserStats(id, username, password, wins, draws, losses));
                // Keep track of the highest user ID
                maxId = Math.max(maxId, id);
            }
        }
         // Set currentUserId to be one more than the highest existing ID
        currentUserId = maxId + 1;
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    // Method to get user stats
    public String getUserStats(String username) {
        UserStats stats = userDB.get(username);
        if (stats != null) {
            return "ID: " + stats.id + ", Wins: " + stats.wins + ", Draws: " + stats.draws + ", Losses: " + stats.losses;
        } else {
            return "User not found!";
        }
    }

    // Method to update user stats
    public void updateUserStats(String username, int wins, int draws, int losses) {
        UserStats stats = userDB.get(username);
        if (stats != null) {
            stats.wins += wins;
            stats.draws += draws;
            stats.losses += losses;
            saveAllUsersToFile(); // Save all users to the file to reflect changes
        }
    }

    // Save all users back to the CSV file after an update
    private void saveAllUsersToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE))) {
            for (Map.Entry<String, UserStats> entry : userDB.entrySet()) {
                UserStats stats = entry.getValue();
                pw.println(stats.id + "," + stats.username + "," + stats.password + "," + stats.wins + "," + stats.draws + "," + stats.losses);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        orb.shutdown(false);
    }

    
    
    
    private Room search(int id){
        for (Room m : matches) {
            boolean res = m.findMe(id);
            if (res) {
                return m;
            }
        }
        return null;
    }
    
    
    
    
    public class Room {
        int[] playerCouples = new int[2];
        int[] lastPiece = new int[2];
        int[] lastMove = new int[2];
        int win=0;
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
    
    
    
    
    
    
    public  class cnxCheck extends Thread {
        public void run() {
            System.out.println("cnxCheck.run() is wornking");
          while(true){
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test1Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
              ServerTimer=ServerTimer+1;
            System.out.println("time="+ServerTimer);
           if(cnx.size()!=0){
            for(int i=0;i<cnx.size();i++){
                cnxArray arr=cnx.get(i);
                if(arr==null){
                    System.out.println("arr is null");
                }else{
                System.out.println("arrray being checked is id : "+arr.id);
                                                    try {
                    //System.out.println("                 cnxCheck.run()       " + (time-arr[1]));
                    arr.access.acquire();
                                                        } catch (InterruptedException ex) {
                                                             Logger.getLogger(Test1Impl.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(ServerTimer-arr.lastAck>2){
                    System.out.println("------------------------------------->"+(ServerTimer-arr.lastAck));                           

                    try {
                        disconnect(arr,arr.id,ServerTimer);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Test1Impl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                arr.access.release();
              }
             }
            }
          }
        }

        
        
    }
    private void disconnect(cnxArray arr , int id,int time) throws InterruptedException {
            Room r=search(id);
            if(r!=null){
               System.out.println("disonnect() id = "+id+"room is "+r.playerCouples[0]+" "+r.playerCouples[1]+" at : "+time);
               int opponentId = (r.playerCouples[0] == id) ? r.playerCouples[1] : r.playerCouples[0];

               // Find the username of the disconnected player
                String disconnectedUsername = null;
                for (Map.Entry<String, UserStats> entry : userDB.entrySet()) {
                    if (entry.getValue().id == id) {
                        disconnectedUsername = entry.getKey();
                        break;
                    }
                }
                
                // Update user stats if username is found
        if (disconnectedUsername != null) {
            updateUserStats(disconnectedUsername, 0, 0, 1);
        }
                
                
               r.lastPiece[0]=-1;
               r.update.release();
               sleep(50);
               r.update.acquire();
               int index=Integer.valueOf(cnx.indexOf(arr));
               cnx.remove(index);
                index=Integer.valueOf(matches.indexOf(r));
                System.out.println("index = "+index);
               matches.remove(index);
            }else{
               int index=Integer.valueOf(cnx.indexOf(arr));
               cnx.remove(index);
            }
        }

    public boolean update(int id){
        System.out.println("///////////////////////////////////////////// updaate id "+id + " at : "+ServerTimer);
            for(int i=0;i<cnx.size();i++){
                cnxArray arr=cnx.get(i);
                if(arr==null){
                    System.out.println("while updating arr is null");
                }
            if(arr.id==id){
                
                                                        try {
                arr.access.acquire();
                                            } catch (InterruptedException ex) {
                                                    Logger.getLogger(Test1Impl.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                arr.lastAck=ServerTimer;
                arr.access.release();
                Room h=search(id);
                if(h==null){
                    cnx.remove(cnx.indexOf(arr));
                    return false;
                }else{
                    return true;
                }
                
            }

        }
        System.out.println("id = "+id+" update has arrived but the room is closed !!!");
        return false;
    }
    
    
  
}
 class cnxArray{
        int id,lastAck;
        Semaphore access=new Semaphore(1); 
        public cnxArray(int id ){
            this.id=id;
            lastAck=Test1Impl.ServerTimer;
            System.out.println("nx array was created > id =  "+id);
        }

}
class UserStats {
    int id;
    String username;
    String password;
    int wins;
    int draws;
    int losses;

    public UserStats(int id, String username, String password, int wins, int draws, int losses) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
    }

}
