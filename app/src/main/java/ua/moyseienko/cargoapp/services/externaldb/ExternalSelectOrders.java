package ua.moyseienko.cargoapp.services.externaldb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExternalSelectOrders {
    public String selectOrders() {
        try {
            URL url = new URL("http://62.122.156.135:5000/cargoapp/api/select-all-orders");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000); // 5 секунд
            con.setReadTimeout(10000); // 10 секунд
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