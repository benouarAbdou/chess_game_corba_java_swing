package testapp;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.ORB;
import testapp.Test1POA;

class Test1Impl extends Test1POA {
    private ORB orb;
    private final String CSV_FILE = "users.csv";
    private final Map<String, UserStats> userDB = new HashMap<>(); // Updated to store UserStats

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

        userDB.put(username, new UserStats(password, 0, 0, 0)); // Initial stats: 0 wins, 0 draws, 0 losses
        saveUserToFile(username, password, 0, 0, 0); // Save user with initial stats
        return true;
    }

    // Method to handle user login
    @Override
    public boolean login(String username, String password) {
        UserStats userStats = userDB.get(username);
        return userStats != null && password.equals(userStats.password);
    }

    // Method to save a new user to the CSV file
    private void saveUserToFile(String username, String password, int wins, int draws, int losses) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE, true))) {
            pw.println(username + "," + password + "," + wins + "," + draws + "," + losses);
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
                if (data.length == 5) {
                    String username = data[0];
                    String password = data[1];
                    int wins = Integer.parseInt(data[2]);
                    int draws = Integer.parseInt(data[3]);
                    int losses = Integer.parseInt(data[4]);
                    userDB.put(username, new UserStats(password, wins, draws, losses));
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
            return "Wins: " + stats.wins + ", Draws: " + stats.draws + ", Losses: " + stats.losses;
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
                pw.println(entry.getKey() + "," + stats.password + "," + stats.wins + "," + stats.draws + "," + stats.losses);
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
    String password;
    int wins;
    int draws;
    int losses;

    public UserStats(String password, int wins, int draws, int losses) {
        this.password = password;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
    }
}
