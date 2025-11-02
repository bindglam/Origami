package com.bindglam.origami.api.utils;

import com.bindglam.origami.api.script.Position;

public final class StringUtil {
    private StringUtil() {
    }

    public static String stringWithArrows(String text, Position posStart, Position posEnd) {
        StringBuilder result = new StringBuilder();

        int idxStart = text.lastIndexOf('\n', posStart.getIdx());
        if (idxStart == -1) {
            idxStart = 0;
        }

        int idxEnd = text.indexOf('\n', idxStart + 1);
        if (idxEnd == -1) {
            idxEnd = text.length();
        }

        int lineCount = posEnd.getLine() - posStart.getLine() + 1;
        for (int i = 0; i < lineCount; i++) {
            String line = text.substring(idxStart, idxEnd);
            int colStart = (i == 0) ? posStart.getColumn() : 0;
            int colEnd = (i == lineCount - 1) ? posEnd.getColumn() : line.length();

            result.append(line).append('\n');

            result.append(" ".repeat(Math.max(0, colStart)));
            result.append("^".repeat(Math.max(0, (colEnd - colStart))));

            result.append('\n');

            idxStart = idxEnd;
            idxEnd = text.indexOf('\n', idxStart + 1);
            if (idxEnd == -1) {
                idxEnd = text.length();
            }
        }

        return result.toString().replace("\t", "");
    }
}
