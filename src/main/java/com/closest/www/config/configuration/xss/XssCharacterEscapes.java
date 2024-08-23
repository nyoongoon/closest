package com.closest.www.config.configuration.xss;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import org.apache.commons.text.translate.CharSequenceTranslator;

/**
 * ObjectMapper 변환을 위한 CharacterEscapes
 * Xss 방지를 위해 이스케이핑 태울 아스키 코드 목록을 asciiEscapes에 설정하고
 * getEscapeSequence()로 이스케이핑한다.
 */
public class XssCharacterEscapes extends CharacterEscapes {
    private final int[] asciiEscapes;
    private final CharSequenceTranslator translator;

    /**
     * ASCII 값 0~127에 대해 기본적인 escape 처리를 설정
     * asciiEscapes배열에
     */
    public XssCharacterEscapes(CharSequenceTranslator translator) {
        // 1. XSS 방지 처리할 특수 문자 지정
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        //ESCAPE_CUSTOM 처리한 아스키문자에 대해서는 getEscapeSequence()를 태운다
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
        this.translator = translator;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        return new SerializedString(translator.translate(Character.toString((char) ch)));
    }
}