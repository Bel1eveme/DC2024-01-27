package by.rusakovich.discussion.model;

public class LanguageToCountryConverter {
    public static String languageToCountry(String language) {
        return language.split(",")[0];
    }
}
