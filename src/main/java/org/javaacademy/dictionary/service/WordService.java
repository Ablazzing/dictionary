package org.javaacademy.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.dictionary.dto.PageDto;
import org.javaacademy.dictionary.dto.WordDto;
import org.javaacademy.dictionary.entity.Word;
import org.javaacademy.dictionary.exception.NotFoundException;
import org.javaacademy.dictionary.repository.WordRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository repository;

    /**
     * Создать слово
     */
    @CacheEvict(cacheNames = "words", allEntries = true)
    public void create(WordDto wordDto) {
        Word entity = new Word(wordDto.getName(), wordDto.getDescription());
        repository.add(entity);
    }

    /**
     * Получение слов (Пагинация)
     */
    @Cacheable(cacheNames = "words")
    public PageDto<List<WordDto>> findAll(int startIndex, int portionSize) {
        List<WordDto> dtos = repository.findAll(startIndex, portionSize).stream()
                .map(this::convert)
                .toList();
        return new PageDto<>(
                startIndex,
                startIndex + portionSize,
                repository.getCountEntities(),
                portionSize,
                dtos
        );
    }

    @Cacheable(cacheNames = "wordByKey")
    public WordDto getWordByKey(String key) {
        return repository.getWord(key)
                .map(this::convert)
                .orElseThrow(() -> new NotFoundException("Нет такого слова"));
    }

    @CacheEvict(cacheNames = {"wordByKey", "words"}, allEntries = true)
    public boolean deleteByKey(String key) {
        return repository.deleteByKey(key);
    }

    @CacheEvict(cacheNames = {"wordByKey", "words"}, allEntries = true)
    public void clearCache() {
    }

    private WordDto convert(Word word) {
        return new WordDto(word.getName(), word.getDescription());
    }
}
