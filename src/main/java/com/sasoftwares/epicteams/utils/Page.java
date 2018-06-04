package com.sasoftwares.epicteams.utils;

import lombok.Data;

@Data
public class Page {

    private String header;
    private String contents;

    public Page(String header, PageContents contents) {
        this.header = header;
        this.contents = contents.contents();
    }
}
