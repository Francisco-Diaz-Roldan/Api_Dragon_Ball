package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Consulta: Del año en que nació el actor anterior, ¿Cuántas películas hay registradas en la base de datos?

/**
 * Realiza una consulta para obtener el número de películas registradas en la base de datos
 * para un año específico.
 */
public class QuintaConsulta {

    /**
     * Método principal que ejecuta la consulta para obtener el número de películas registradas en 1982.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        try {
            quintaConsulta();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Realiza la consulta para obtener el número de películas registradas en la base de datos
     * para el año 1982.
     *
     * @throws IOException            Si ocurre un error de entrada/salida durante la consulta HTTP.
     * @throws InterruptedException   Si el hilo actual es interrumpido mientras espera la respuesta.
     */
    private static void quintaConsulta() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/byYear/1982/"))
                .header("X-RapidAPI-Key", "01d02f25a8msh7a4ca081b7e5405p14f6ddjsnadbf679b6428")
                .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Analiza la respuesta JSON
        JSONObject jsonResponse = new JSONObject(response.body());

        // Comprueba que la respuesta contiene el campo "results" y obtener el número de películas
        if (jsonResponse.has("results")) {
            int numPeliculas = jsonResponse.getJSONArray("results").length();
            System.out.println("Número de películas registradas en 1982: " + numPeliculas);
        } else {
            System.out.println("No existen resultados.");
        }
    }
}