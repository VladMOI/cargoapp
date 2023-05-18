package ua.moyseienko.cargoapp.services.externaldb;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ExternalInsertUser {
    public String createUser(String firstName, String lastName, String email, String experience, String balance, String password) {
        System.out.println("Opening cnnection to API...");
        StringBuilder response = new StringBuilder();
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL("http://62.122.156.135:5000/cargoapp/api/create-user");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000); // 5 секунд
            con.setReadTimeout(10000); // 10 секунд
            HashMap<String, String> map = new HashMap<>();
            map.put("firstname", firstName);
            map.put("lastname", lastName);
            map.put("email", email);
            map.put("balance", balance);
            map.put("experience", experience);
            map.put("password", password);
            JSONObject jsonEmail = new JSONObject(map);
            String jsonEmailStr = jsonEmail.toString();
            try (DataOutputStream outputStream = new DataOutputStream(con.getOutputStream())) {
                outputStream.writeBytes(jsonEmailStr);
                outputStream.flush();
            }

            int responseCode = con.getResponseCode();
            System.out.println("Код ответа: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
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
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                System.out.println("Закрытие соединения...");
                con.disconnect();
            }
        }
        return "";
    }
}
