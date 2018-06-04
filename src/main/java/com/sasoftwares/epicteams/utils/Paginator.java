package com.sasoftwares.epicteams.utils;

import com.sasoftwares.epicteams.Team;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.List;

public class Paginator {

    private int amountPerPage;
    @Getter
    private int maxPages;
    private List<?> list;

    public Paginator(List<?> list, int amountPerPage) {
        this.amountPerPage = amountPerPage;
        this.list = list;
    }

    public Page getPage(int page) {
        --page;
        maxPages = list.size() / amountPerPage;
        if (list.size() % amountPerPage == 0) {
            maxPages--;
        }
        final int pageNumb = Math.max(0, Math.min(page, maxPages));
        return new Page(
                ChatColor.GRAY + "- " + ChatColor.AQUA + "Page: " + ChatColor.GRAY
                        + "(" + ChatColor.YELLOW + (page + 1) + ChatColor.GRAY + '/'
                        + ChatColor.YELLOW + (maxPages + 1) + ChatColor.GRAY + ')', () -> {
            StringBuilder sb = new StringBuilder(amountPerPage);
            for (int i = amountPerPage * pageNumb; i < amountPerPage * pageNumb + amountPerPage && i < list.size(); i++) {
                Team team = (Team) list.get(i);
                sb.append(ChatColor.GRAY).append("- ").append(ChatColor.YELLOW).append(team.getName()).append('\n');
            }
            return sb.toString();
        });
    }
}
