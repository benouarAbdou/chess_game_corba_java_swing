package testapp;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.ORB;
import testapp.Test1POA;

class Test1Impl extends Test1POA {
    private ORB orb;
    private final String CSV_FILE = "users.csv";
    private final Map<String, UserStats> userDB = new HashMap<>();
    private int currentUserId = 0; // Counter for user IDs

    public void setORB(ORB orb_val) {
        orb = orb_val;
        loadUsersFromFile();
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
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) { // Check for 6 fields: id, username, password, wins, draws, losses
                    int id = Integer.parseInt(data[0]);
                    String username = data[1];
                    String password = data[2];
                    int wins = Integer.parseInt(data[3]);
                    int draws = Integer.parseInt(data[4]);
                    int losses = Integer.parseInt(data[5]);

                    // Ensure the currentUserId is higher than the last loaded ID
                    currentUserId = Math.max(currentUserId, id + 1);

                    // Put the user into the userDB
                    userDB.put(username, new UserStats(id, username, password, wins, draws, losses));
                }
            }
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
