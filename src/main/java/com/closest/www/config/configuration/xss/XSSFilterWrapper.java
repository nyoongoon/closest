package com.closest.www.config.configuration.xss;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.Part;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

public class XSSFilterWrapper extends HttpServletRequestWrapper {
    // 이스케이프를 위한 Translator
    private final CharSequenceTranslator translator;
    private byte[] rawData;
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XSSFilterWrapper(HttpServletRequest request, CharSequenceTranslator translator) {
        super(request);
        this.translator = translator;

        try {
            String contentType = request.getContentType();
            if(StringUtils.isNotBlank(contentType)  && contentType.equals(TEXT_PLAIN_VALUE)) {
                InputStream is = request.getInputStream();
                this.rawData = replaceXSS(inputStreamToByteArray(is));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] replaceXSS(byte[] data) {
        String strData = new String(data);
        return replaceXSS(strData).getBytes();
    }

    private String replaceXSS(String value) {
        if (value != null) {
            value = this.translator.translate(value);
        }
        return value;
    }

    //새로운 인풋스트림을 리턴하지 않으면 에러가 남
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(this.rawData == null) {
            return super.getInputStream();
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                // TODO Auto-generated method stub
                return byteArrayInputStream.read();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // TODO Auto-generated method stub
            }

            @Override
            public boolean isReady() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isFinished() {
                // TODO Auto-generated method stub
                return false;
            }
        };
    }

    @Override
    public String getQueryString() {
        return replaceXSS(super.getQueryString());
    }


    @Override
    public String getParameter(String name) {
        return replaceXSS(super.getParameter(name));
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        Collection<Part> parts = super.getParts();
        Map<String, String> escapedParameters = new HashMap<>();

        for (Part part : parts) {
            System.out.println(part.getContentType());
            // 파일인 경우는 그대로 넘기고, 그 외의 파라미터에 대해 XSS 필터링
            if (part.getSubmittedFileName() == null) {
                String partName = part.getName();
                String partValue = new String(part.getInputStream().readAllBytes());
                String escapedValue = replaceXSS(partValue);
                escapedParameters.put(partName, escapedValue);
            }
        }

        // 모든 파라미터들에 대해 escape 처리된 파라미터들로 대체
        for (Map.Entry<String, String> entry : escapedParameters.entrySet()) {
//            PartWrapper escapedPart = new PartWrapper(entry.getKey(), entry.getValue());
//            parts.removeIf(p -> p.getName().equals(entry.getKey()));
//            parts.add(escapedPart);
        }

        return parts;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> params = super.getParameterMap();
        if (params != null) {
            params.forEach((key, value) -> {
                for (int i = 0; i < value.length; i++) {
                    value[i] = replaceXSS(value[i]);
                }
            });
        }
        return params;
    }


    @Override
    public String[] getParameterValues(String name) {
        String[] params = super.getParameterValues(name);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                params[i] = replaceXSS(params[i]);
            }
        }
        return params;
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
    }

    private byte[] inputStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];  // 1KB씩 읽어들이기 위한 버퍼 크기

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }
}

