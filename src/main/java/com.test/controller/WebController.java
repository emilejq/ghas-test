package com.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/sast")
@RestController
@Validated
public class WebController {

  @GetMapping(path = "/login")
  public @ResponseBody ResponseEntity<String> login(@RequestParam(name = "username") String username) {
    return ResponseEntity.ok(String.format("Welcome back %s!", "username"));
  }

  @GetMapping(path = "/picture/{user}")
  public @ResponseBody ResponseEntity<FileInputStream> getImage(@PathVariable(name = "user") String user,
      @RequestParam(name = "location", required = false) String location) {
    try {
      FileInputStream stream = null;

      if (location != null) {
        try {
          URL url = new URL(location);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("GET");
          stream = (FileInputStream) connection.getInputStream();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        File file = new File("resources/images/", user);
        stream = new FileInputStream(file);
      }
      return ResponseEntity.ok().body(stream);
    } catch (FileNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping(path = "/save-details")
  public @ResponseBody ResponseEntity<Void> saveUserDetails(@RequestParam(name = "user_path") String path) {

    try {
      String command = "cmd.exe /c dir " + path;
      if (command.contains("cmd.exe /c dir ")) {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        log.info(stdInput.readLine());
      }
    }
    catch (IOException e) {
      System.out.println("Error executing command");
    }
    return ResponseEntity.ok().build();
  }
}
