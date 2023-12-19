import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class Api {
    public static String[] Kryptowaluty = FileHandler.wczytajZPliku();

    public static List<Double> getKurs(List<String> nazwyKryptowalut) throws IOException {
        String joinedNames = String.join(",", nazwyKryptowalut);
        URL url = new URL("https://api.coingecko.com/api/v3/simple/price?ids=" + joinedNames + "&vs_currencies=USD");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        System.out.println("HTTP Response Code: " + responseCode);

        InputStream inputStream;

        try {
            inputStream = connection.getInputStream();
        } catch (Exception ex) {
            inputStream = connection.getErrorStream();
        }

        StringBuilder responseBody = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            responseBody.append(new String(buffer, 0, bytesRead));
        }
        inputStream.close();
        return parseApiResponse(responseBody.toString());
    }
    private static List<Double> parseApiResponse(String response) {
        List<Double> prices = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(response);

            for (Transaction transaction : Main.tranzakcje) {
                String cryptocurrencyName = transaction.getName();

                if (jsonResponse.has(cryptocurrencyName)) {
                    double currentPrice = jsonResponse.getJSONObject(cryptocurrencyName).getDouble("usd");
                    prices.add(currentPrice);
                } else {
                    System.out.println("No data found for cryptocurrency: " + cryptocurrencyName);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return prices;
    }
}
