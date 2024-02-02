package com.example.myapplication.myword;

import com.example.myapplication.myword.WordDefinition;

import java.util.List;

public class WordMeaning {
    public String  partOfSpeech;
    public List<WordDefinition>  definitions;

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
