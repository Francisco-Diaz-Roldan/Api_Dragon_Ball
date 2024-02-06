package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Consulta: Lugar de nacimiento y signo del zodiaco del actor que hacía de Goku en la pelicula "Dragonball Evolution"


/**
 * Clase que realiza consultas relacionadas con la película "Dragonball Evolution" y el actor que interpreta a Goku.
 */
public class CuartaConsulta {
    /**
     * Método principal que inicia las consultas y muestra los resultados.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        System.out.println(obtenerPeliculaPorNombre());
        System.out.println(obtenerActorGokuIdPorImdbId("tt1098327"));
        System.out.println(obtenerInfoActorGoku("nm0154226"));
    }

    /**
     * Realiza una consulta para obtener el IMDb ID de la película "Dragonball Evolution".
     *
     * @return El IMDb ID de la película.
     */
    public static String obtenerPeliculaPorNombre() {
        String res = "";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/imdb_id/byTitle" +
                            "/Dragonball%20Evolution/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers
                    .ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            // Obtener el array "results" de la respuesta JSON
            JSONArray resultsArray = jsonResponse.getJSONArray("results");

            // Verificar si hay al menos un elemento en el array
            if (!resultsArray.isEmpty()) {
                // Obtener el IMDb ID directamente del primer elemento del array
                String movieId = resultsArray.getJSONObject(0).getString("imdb_id");
                System.out.println("IMDb ID: " + movieId);

                res = movieId;

                // Imprimir la respuesta JSON formateada
                System.out.println(jsonResponse.toString(2));
            } else {
                System.out.println("No se encontraron resultados para la película.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Realiza una consulta para obtener el IMDb ID del actor que interpreta a Goku en la película.
     *
     * @param imdbIdPelicula El IMDb ID de la película.
     * @return El IMDb ID del actor que interpreta a Goku.
     */
    public static String obtenerActorGokuIdPorImdbId(String imdbIdPelicula) {

        String actor = "";

        try {
            // Construir la solicitud HTTP para obtener el elenco de la película con el IMDb ID
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/movie/id/" + imdbIdPelicula + "/cast/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            // Realizar la solicitud HTTP y obtener la respuesta en formato JSON
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers
                    .ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            if (jsonResponse.has("results")) {
                JSONArray rolesArray = jsonResponse.getJSONObject("results").getJSONArray("roles");

                // Buscar el actor que interpreta el papel de Goku
                for (int i = 0; i < rolesArray.length(); i++) {
                    JSONObject role = rolesArray.getJSONObject(i);

                    if ("Goku".equalsIgnoreCase(role.getString("role"))) {
                        actor = role.getJSONObject("actor").getString("imdb_id");
                        System.out.println("id del actor de goku: " + actor);
                        break;  // Terminar la búsqueda una vez que se encuentra a Gokú
                    }
                }
            } else {
                System.out.println("La respuesta no contiene resultados.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actor;
    }

    /**
     * Realiza una consulta para obtener la información del actor que interpreta a Goku.
     *
     * @param imdbIdActor El IMDb ID del actor.
     * @return Una cadena JSON con la información seleccionada del actor, que incluye el lugar de nacimiento y el signo
     * del zodiaco.
     * Si la consulta es exitosa, se devuelve la representación JSON formateada; de lo contrario, se devuelve un
     * mensaje de error.
     */
    public static String obtenerInfoActorGoku(String imdbIdActor) {
        try {
            // Construir la solicitud HTTP para obtener la información del actor con el IMDb ID
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://moviesminidatabase.p.rapidapi.com/actor/id/" + imdbIdActor + "/"))
                    .header("X-RapidAPI-Key", APIConfig.API_KEY)
                    .header("X-RapidAPI-Host", "moviesminidatabase.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            // Realizar la solicitud HTTP y obtener la respuesta en formato JSON
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers
                    .ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            if (jsonResponse.has("results")) {
                JSONObject actor = jsonResponse.getJSONObject("results");

                // Crear un nuevo JSONObject solo con los campos requeridos
                JSONObject selectedFields = new JSONObject();
                selectedFields.put("birth_place", actor.getString("birth_place"));
                selectedFields.put("star_sign", actor.getString("star_sign"));

                // Devolver la representación JSON seleccionada
                return selectedFields.toString(2);
            } else {
                return "La respuesta no contiene resultados.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener la información del actor.";
        }
    }
}