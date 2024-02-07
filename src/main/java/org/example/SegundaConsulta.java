package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//Consulta: La url del poster de la primera serie que aparece registrada (del 1995).

/**
 * Clase que realiza consultas a una API para obtener información sobre series y mostrar la URL del poster
 * de la primera serie registrada en el año 1995.
 */
public class SegundaConsulta {

    /**
     * Método principal que realiza las operaciones necesarias para imprimir la URL del poster de la primera serie
     * registrada en 1995.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        imprimirURLPoster(obtenerIdSerie());
    }

    /**
     * Obtiene el ID de la serie "Dragon Ball" registrada en 1995.
     *
     * @return El ID de la serie "Dragon Ball".
     */
    private static String obtenerIdSerie() {
        String idSerie = "";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/series/byYear/1995/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray jsonArray = jsonResponse.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject serie = jsonArray.getJSONObject(i);
                String title = serie.getString("title");

                // Compruebo que el título sea "Dragon Ball"
                if ("Dragon Ball".equalsIgnoreCase(title)) {
                    // Guardo el ID de la serie
                    idSerie = serie.getString("imdb_id");
                    break;  // La iteración acaba cuando encuentra la serie "Dragon Ball"
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idSerie;
    }

    /**
     * Imprime la URL del poster de la serie utilizando el ID proporcionado.
     *
     * @param idSerie El ID de la serie para la cual se imprimirá la URL del poster.
     * @return Un objeto JSON vacío (puede ser usado para futuras extensiones).
     */
    private static JSONObject imprimirURLPoster(String idSerie) {
        JSONObject resultObject = new JSONObject();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/series/id/" + idSerie + "/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            String bannerUrl = jsonResponse.getJSONObject("results").getString("banner");

            System.out.print("URL del poster: " + bannerUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObject;
    }
}
