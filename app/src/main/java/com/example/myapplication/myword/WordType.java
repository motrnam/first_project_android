package com.example.myapplication.myword;

public enum WordType {
    NOUN("noun"),
    VERB("verb"),
    ADJECTIVE("adjective"),
    OTHER("other");
    final String name;

    WordType(String name) {
        this.name = name;
    }

    public static WordType getWordTypeByName(String theName) {
        for (WordType wordType : WordType.values())
            if (wordType.name.equals(theName)) return wordType;
        return null;
    }
}
