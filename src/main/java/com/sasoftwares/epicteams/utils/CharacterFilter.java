package com.sasoftwares.epicteams.utils;

import com.sasoftwares.epicteams.managers.FileManager;

import java.util.regex.Pattern;

public class CharacterFilter {
    private CharacterFilter() {
    }

    public static boolean hasSpecialCharacter(String text) {
        return Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]").matcher(text).find() && !FileManager.i.getConfig().getBoolean("allow-special-characters");
    }

    public static boolean hasInteger(String text) {
        return Pattern.compile("[0-9]").matcher(text).find() && !FileManager.i.getConfig().getBoolean("allow-integers");
    }
}
