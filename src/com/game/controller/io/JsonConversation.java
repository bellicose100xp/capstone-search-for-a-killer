package com.game.controller.io;

import java.util.List;
import java.util.Random;

import com.game.model.Character;

public class JsonConversation {
    private List<String> greetings;
    private List<String> introductions;
    private List<String> farewells;
    private List<String> inquiries;

    public String getRandomGreeting(Character character) {
        return getRandomElement(greetings).replace("$name", character.getName());
    }

    public String getRandomIntroduction(Character character) {
        return getRandomElement(introductions).replace("$name", character.getName());
    }

    public String getRandomFarewell(Character character) {
        return getRandomElement(farewells).replace("$name", character.getName());
    }

    public String getRandomInquiry() {
        return getRandomElement(inquiries);
    }

    private String getRandomElement(List<String> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List is empty or null");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    public List<String> getGreetings() {
        return greetings;
    }

    public void setGreetings(List<String> greetings) {
        this.greetings = greetings;
    }

    public List<String> getIntroductions() {
        return introductions;
    }

    public void setIntroductions(List<String> introductions) {
        this.introductions = introductions;
    }

    public List<String> getFarewells() {
        return farewells;
    }

    public void setFarewells(List<String> farewells) {
        this.farewells = farewells;
    }

    public List<String> getInquiries() {
        return inquiries;
    }

    public void setInquiries(List<String> inquiries) {
        this.inquiries = inquiries;
    }
}
