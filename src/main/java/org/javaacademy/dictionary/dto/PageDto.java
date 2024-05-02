package org.javaacademy.dictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageDto <T> {
    private Integer startPosition;
    private Integer endPosition;
    private Integer totalSize;
    private Integer portionSize;
    private T content;
}
