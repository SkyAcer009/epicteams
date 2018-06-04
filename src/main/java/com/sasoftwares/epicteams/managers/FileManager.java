package com.sasoftwares.epicteams.managers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class FileManager {

    public static FileManager i = new FileManager();
    @Getter
    private File configFile, databaseFolder, languageFile;
    @Getter
    private FileConfiguration config, language;
    private Plugin plugin;

    private FileManager() {
    }

    public void setupConfig(Plugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdir())
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Failed to create plugin directory!");

        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        this.loadConfig();
        this.saveConfig();
        this.loadConfig();

    }

    public void setupDatabase(Plugin plugin) {
        this.plugin = plugin;
        this.databaseFolder = new File(plugin.getDataFolder(), "database");
        if (!databaseFolder.exists() && !databaseFolder.mkdir())
            Bukkit.getLogger().severe(ChatColor.RED + "Failed to create plugin database directory!");
    }

    public void setupLanguage(Plugin plugin) {
        this.plugin = plugin;
        this.languageFile = new File(plugin.getDataFolder(), "lang_en.yml");
        this.loadLanguage();
        this.saveLanguage();
        this.loadLanguage();
    }

    public void saveConfig() {
        if (!this.configFile.exists()) {
            this.plugin.saveResource("config.yml", false);
        }
    }

    public void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveLanguage() {
        if (!this.languageFile.exists()) {
            this.plugin.saveResource("lang_en.yml", false);
        }
    }

    public void loadLanguage() {
        this.language = YamlConfiguration.loadConfiguration(this.languageFile);
    }

    public String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
