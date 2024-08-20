package com.closest.www.config.configuration.xss;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.CharSequenceTranslator;

public class XssCharacterEscapes extends CharacterEscapes {
    private final int[] asciiEscapes;
    private final CharSequenceTranslator translator;

    /**
     * ASCII 값 0~127에 대해 기본적인 escape 처리를 설정
     * EntityArrays#BASIC_ESCAPE 기반 esacpe 처리를 확장
     */
    public XssCharacterEscapes(CharSequenceTranslator translator) {
        // 0 ~ 127(ascii) 설정
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['&'] = CharacterEscapes.ESCAPE_CUSTOM;

        this.translator = translator;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        /**
         * 아래 커스텀을 CharSequenceTranslator으로 옮기기..
         */
        SerializedString serializedString;
        char charAt = (char) ch;

        // emoji jackson parse error 처리
        if (Character.isHighSurrogate(charAt) || Character.isLowSurrogate(charAt)) {
            StringBuffer sb = new StringBuffer();
            sb.append("\\u");
            sb.append(String.format("%04x", ch));
            serializedString = new SerializedString(sb.toString());
        } else {
            switch (ch) {
                // middot('·') 예외 처리
                case 183 ->
                        serializedString = new SerializedString(StringEscapeUtils.unescapeHtml4(Character.toString(charAt)));
                // bullet('•'), bullet operator('∙'), dot operator('∙'), white bullet('◦')특수문자 middot('·')으로 치환 (KO16MSWIN949 미지원)
                case 8226, 8729, 8901, 9702 -> serializedString = new SerializedString("·");
                default ->
                        serializedString = new SerializedString(this.translator.translate(Character.toString(charAt)));
            }
        }
        return serializedString;
    }
}


