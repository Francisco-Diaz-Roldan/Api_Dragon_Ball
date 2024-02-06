package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Consulta: De ese mismo año es la película Postergeist, el primer gran éxito de Steven Spielberg como productor,
// que participó en los premios Oscars al año siguiente sin ser ganadora. ¿A qué apartado fue nominada?
// ¿Que otro premio ganó ese año?

/**
 * Clase que realiza consultas relacionadas con la película Poltergeist y sus premios.
 */
public class SextaConsulta {

    /**
     * Método principal que inicia las consultas y muestra los resultados.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        try {
            // Obtener el ID de la película Poltergeist
            String idPoltergeist = obtenerIdPoltergeist();
            System.out.println("ID de Poltergeist: " + idPoltergeist);

            // Obtener y mostrar los premios de la película
            obtenerPremios(idPoltergeist);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza una consulta para obtener el ID de la película Poltergeist.
     *
     * @return El ID de la película Poltergeist.
     * @throws IOException          Si hay un error de entrada/salida durante la consulta.
     * @throws InterruptedException Si la operación es interrumpida mientras espera.
     */
    private static String obtenerIdPoltergeist() throws IOException, InterruptedException {
        String idPoltergeist = "";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/imdb_id/byTitle/poltergeist/"))
                .header("X-RapidAPI-Key", "01d02f25a8msh7a4ca081b7e5405p14f6ddjsnadbf679b6428")
                .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Analiza la respuesta JSON
        JSONObject jsonResponse = new JSONObject(response.body());

        // Comprueba si la respuesta contiene el campo "results"
        if (jsonResponse.has("results")) {
            // Obtiene el ID directamente del primer elemento del array
            idPoltergeist = jsonResponse.getJSONArray("results").getJSONObject(0).getString("imdb_id");
        } else {
            System.out.println("No se encontraron resultados para la película Poltergeist.");
        }
        return idPoltergeist;
    }

    /**
     * Realiza una consulta para obtener los premios de una película por su ID.
     *
     * @param idPelicula El ID de la película.
     * @return Una cadena que contiene la información de los premios.
     * @throws IOException          Si hay un error de entrada/salida durante la consulta.
     * @throws InterruptedException Si la operación es interrumpida mientras espera.
     */
    private static String obtenerPremios(String idPelicula) throws IOException, InterruptedException {
        String premios = "";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/id/" + idPelicula + "/awards/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers
                    .ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            System.out.println(jsonResponse.toString(2));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return premios;
    }
}