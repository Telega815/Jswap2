package ru.jswap.services.Implementations;

import org.springframework.stereotype.Service;
import ru.jswap.services.interfaces.HtmlGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HtmlGeneratorImpl implements HtmlGenerator {
    private String elementType;
    private String innerText = "";
    private List<String> attrNames;
    private List<String> attrValues;
    private boolean fullTag;


    public HtmlGeneratorImpl(String elementType) {
        attrNames = new ArrayList<>();
        attrValues = new ArrayList<>();
        this.elementType = elementType;
        this.fullTag = false;
    }

    @Override
    public void setElementType(String type) {
        elementType = type;
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {
        attrNames.add(attrName);
        attrValues.add(attrValue);
    }

    @Override
    public void setInnerText(String innerText) {
        this.innerText = innerText;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("<");

        res.append(elementType + " ");
        res.append(getAttributes());
        if (Objects.equals(innerText, "")){
            if(fullTag) {
                res.append("></");
                res.append(elementType);
                res.append(">");
            }else {
                res.append("/>");
            }
        }else{
            res.append(">");
            res.append(innerText);
            res.append("</");
            res.append(elementType);
            res.append(">");
        }
        return res.toString();
    }

    private String getAttributes(){
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < attrNames.size(); i++) {
            res.append(getAttribute(i));
        }
        return res.toString();
    }

    private String getAttribute(int index){
        StringBuilder res = new StringBuilder(attrNames.get(index));
        res.append("=\"");
        res.append(attrValues.get(index));
        res.append("\" ");
        return res.toString();
    }

    @Override
    public void setFullTag(boolean fullTag) {
        this.fullTag = fullTag;
    }
}
