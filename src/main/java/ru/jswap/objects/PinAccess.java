package ru.jswap.objects;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class PinAccess {
    private Set<Long> accessedPages;

    public PinAccess() {
        accessedPages = new HashSet<>(5);
    }

    public boolean addNewPage(Long userid){
        return accessedPages.add(userid);
    }

    public boolean contains(Long userid){
        return accessedPages.contains(userid);
    }
}
