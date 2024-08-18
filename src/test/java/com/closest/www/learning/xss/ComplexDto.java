package com.closest.www.learning.xss;

import java.util.List;

public class ComplexDto {

    private XssRequestDto mainContent;
    private List<XssRequestDto> subContents;

    // Constructors, Getters, Setters

    public ComplexDto() {
    }

    public ComplexDto(XssRequestDto mainContent, List<XssRequestDto> subContents) {
        this.mainContent = mainContent;
        this.subContents = subContents;
    }

    public XssRequestDto getMainContent() {
        return mainContent;
    }

    public void setMainContent(XssRequestDto mainContent) {
        this.mainContent = mainContent;
    }

    public List<XssRequestDto> getSubContents() {
        return subContents;
    }

    public void setSubContents(List<XssRequestDto> subContents) {
        this.subContents = subContents;
    }
}
