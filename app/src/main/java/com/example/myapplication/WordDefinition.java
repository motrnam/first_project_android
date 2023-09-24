package com.example.myapplication;

import java.util.List;

public class WordDefinition {
    String definition;
    String example;
    List<String> synonyms;
    List<String> antonyms;

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        if (example == null)
            return "example: ";
        return "example: "+example;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public WordDefinition(String definition, String example, List<String> synonyms, List<String> antonyms) {
        this.definition = definition;
        this.example = example;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
    }

    public String getSynonymsInStringFormat() {
        if (synonyms == null || synonyms.isEmpty())
            return "synonyms: ";
        if (synonyms.size() == 1)
            return "synonym: " + synonyms.get(0);
        StringBuilder resultOfOutput = new StringBuilder();
        resultOfOutput.append("synonym: ");
        for (int i = 0; i < synonyms.size() - 1; i++)
            resultOfOutput.append(synonyms.get(i)).append(" , ");
        resultOfOutput.append(synonyms.get(synonyms.size() - 1));
        return resultOfOutput.toString();
    }

    public String getAntonymsInStringFormat() {
        if (antonyms == null || antonyms.isEmpty())
            return "Antonyms: ";
        if (antonyms.size() == 1)
            return "Antonyms: " + antonyms.get(0);
        StringBuilder resultOfOutput = new StringBuilder();
        resultOfOutput.append("Antonyms: ");
        for (int i = 0; i < antonyms.size() - 1; i++)
            resultOfOutput.append(antonyms.get(i)).append(" , ");
        resultOfOutput.append(antonyms.get(antonyms.size() - 1));
        return resultOfOutput.toString();
    }
}
