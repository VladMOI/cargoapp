package ua.moyseienko.cargoapp.services.externaldb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ExternalSelectUser {
    public String selectUser(String uEmail) {
        try {
            URL url = new URL("http://62.122.156.135:5000/cargoapp/api/select-user-email");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = "{\"email\": \"" + uEmail + "\"}";

            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            System.out.println("Код ответа: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                if (response.length() > 0) {
                    System.out.println("Ответ сервера: " + response.toString());
                    return response.toString();
                } else {
                    System.out.println("Произошла ошибка: ответ от сервера пустой.");
                }
            } else {
                System.out.println("Произошла ошибка: код ответа сервера - " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
        return "";
    }

}
