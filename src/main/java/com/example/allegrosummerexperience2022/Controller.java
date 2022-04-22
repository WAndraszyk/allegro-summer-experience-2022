package com.example.allegrosummerexperience2022;

import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Klasa kontroler REST
 */
@RestController
public class Controller {

    /**
     * Funkcja obsługuje żądanie filterOut
     *
     * @param login    Nazwa użytkownika GitHub
     */
    @GetMapping("/{login}")
    @ResponseBody
    public String giveInfo(@PathVariable String login) throws IOException {
        String url = "https://api.github.com/users/" + login;
        URL realURL = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(realURL.openStream()));
        String json = in.readLine();
        in.close();
        JsonInterpreter interpreter = new JsonInterpreter(json, url) ;
        return interpreter.getInfo();
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }
}
