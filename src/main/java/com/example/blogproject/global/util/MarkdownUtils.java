package com.example.blogproject.global.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

public class MarkdownUtils {
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .softBreak("<br/>")
                .build();
        return renderer.render(document);
    }

    public static String removeMarkdown(String markdown) {
        // 마크다운 서식을 제거하고 플레인 텍스트로 변환하는 로직
        // 예: 헤더(#), 강조(*,_), 링크([]()), 코드 블록(```) 등을 제거
        String plainText = markdown.replaceAll("#", "")
                .replaceAll("[*_]", "")
                .replaceAll("\\[.*?\\]\\(.*?\\)", "")
                .replaceAll("`{1,3}[\\s\\S]*?`{1,3}", "")
                .replaceAll("\\n", " ")
                .replaceAll("\\s+", " ")
                .trim();
        return plainText;
    }
}