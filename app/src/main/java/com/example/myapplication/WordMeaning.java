package com.example.myapplication;

import java.util.List;

public class WordMeaning {
    String partOfSpeech;
    List<WordDefinition>  definitions;

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public List<WordDefinition> getDefinitions() {
        return definitions;
    }

    public WordMeaning(String partOfSpeech, List<WordDefinition> definitions) {
        this.partOfSpeech = partOfSpeech;
        this.definitions = definitions;
    }
}
