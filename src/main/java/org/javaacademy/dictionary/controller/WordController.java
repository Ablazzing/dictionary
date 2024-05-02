package org.javaacademy.dictionary.controller;

import lombok.RequiredArgsConstructor;
import org.javaacademy.dictionary.dto.PageDto;
import org.javaacademy.dictionary.dto.WordDto;
import org.javaacademy.dictionary.exception.NotFoundException;
import org.javaacademy.dictionary.service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dictionary")
public class WordController {
    private final WordService wordService;

    @PostMapping
    public ResponseEntity<?> createWord(@RequestBody WordDto dto) {
        wordService.create(dto);
        return accepted().build();
    }

    @GetMapping
    public PageDto<List<WordDto>> getWords(
            @RequestParam Integer startPosition,
            @RequestParam Integer portionSize) {
        return wordService.findAll(startPosition, portionSize);
    }

    @GetMapping("/{key}")
    public ResponseEntity<?> getWordByKey(@PathVariable String key) {
        try {
            return ResponseEntity.ok(wordService.getWordByKey(key));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<?> deleteByKey(@PathVariable String key) {
        if (!wordService.deleteByKey(key)) {
            return notFound().build();
        }
        return accepted().build();
    }
}
