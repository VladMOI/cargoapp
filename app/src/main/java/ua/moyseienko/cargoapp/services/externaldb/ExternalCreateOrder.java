package ua.moyseienko.cargoapp.services.externaldb;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ExternalCreateOrder {
    private static final String TAG = "ExternalCreateOrder";

    public void createOrder(String addressFrom, String addressTo,
                            String sentAt, String receivedAt,
                            String price, String distance,
                            String packSize, String packType, String packWeight,
                            String driver) {

        HashMap<String, String> map = new HashMap<>();
        map.put("addressFrom", addressFrom);
        map.put("addressTo", addressTo);
        map.put("sendingDate", sentAt);
        map.put("recievedDate", receivedAt);
        map.put("price", price);
        map.put("distance", distance);
        map.put("packageSize", packSize);
        map.put("packageType", packType);
        map.put("packageWeight", packWeight);
        map.put("driver", driver);
        // Выполнение POST-запроса в фоновом потоке
        new CreateOrderTask().execute(map);
    }

    private class CreateOrderTask extends AsyncTask<HashMap<String, String>, Void, String> {

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();

            try {
                URL url = new URL("http://62.122.156.135:5000/cargoapp/api/create-order");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                HashMap<String, String> map = params[0];
                JSONObject json = new JSONObject(map);

                String requestBody = json.toString();
                System.out.println("Request body = " + requestBody);
                // Отправка тела запроса на сервер
                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                    outputStream.writeBytes(requestBody);
                    outputStream.flush();
                }

                // Получение ответа от сервера
                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Response Code: " + responseCode);

                // Чтение ответа сервера
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Произошла ошибка: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // Обработка ответа от сервера
            Log.d(TAG, "Response Body: " + result);
        }
    }
    public interface SelectOrdersCallback {
        void onOrdersSelected(String result);
    }
}