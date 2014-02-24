package com.engineer.sun.guava.base.escape;

import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;

public class Escape{
    public static void main(String[] args) {
        Escaper escaper = HtmlEscapers.htmlEscaper();
        String escape = escaper.escape("<a>baidu<a>");
        System.out.println(escape);
    }

}
