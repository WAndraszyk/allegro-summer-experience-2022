package com.example.allegrosummerexperience2022;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Interpreter JSON
 */
public class JsonInterpreter {

    /**
     * Odczytany JSON.
     */
    private final JsonNode jsonNode;

    /**
     * Mapper do odczytywania JSONów.
     */
    public ObjectMapper mapper;

    private final String reposUrl;

    /**
     * Konstruktor parsujący dane w formacie JSON ze Stringa na JsonNode'a
     * @param json String zawierający JSON
     */
    public JsonInterpreter(String json, String url) throws JsonProcessingException {
        this.mapper = new ObjectMapper();
        this.jsonNode = mapper.readTree(json);
        this.reposUrl = url + "/repos";
    }

    public String getInfo() throws IOException {
        String login = jsonNode.get("login").asText();
        StringBuilder info = new StringBuilder("Login: " + login + "<br>");
        info.append("Nazwa:").append(jsonNode.get("name").asText()).append("<br>");
        info.append("Bio:").append(jsonNode.get("bio").asText()).append("<br>");
        URL realURL = new URL(reposUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(realURL.openStream()));
        String array = in.readLine();
        in.close();
        JsonNode arrayNode = mapper.readTree(array);
        List<String> reposArray = new ArrayList<>();
        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                reposArray.add(jsonNode.get("name").asText());
            }
        }
        List<Integer> langsBites = new ArrayList<>();
        List<String> langs = new ArrayList<>();
        StringBuilder repos = new StringBuilder("Repozytoria: <br>");
        for (String repo : reposArray){
            repos.append("&nbsp&nbsp&nbsp").append(repo).append(": <br>");
            URL repoUrl = new URL("https://api.github.com/repos/" + login + "/" + repo + "/languages");
            BufferedReader input = new BufferedReader(new InputStreamReader(repoUrl.openStream()));
            String repoString = input.readLine();
            in.close();
            JsonNode repoJson = mapper.readTree(repoString);
            Iterator<String> itr = repoJson.fieldNames();
            while(itr.hasNext()) {
                String lang = itr.next();
                Integer bites = repoJson.get(lang).asInt();
                System.out.println(repoJson.get(lang).asText());
                if(!langs.contains(lang)){
                    langs.add(lang);
                    langsBites.add(bites);
                }
                else {
                    int i = langs.indexOf(lang);
                    langsBites.set(i, langsBites.get(i) + bites);
                }
                repos.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp").append(lang).append(" : ").append(bites).append("<br>");
            }
        }
        info.append("Języki: <br>");
        for (String lang : langs){
            info.append("&nbsp&nbsp&nbsp").append(lang).append("<br>");
        }
        info.append(repos);
        return info.toString();
    }
}
