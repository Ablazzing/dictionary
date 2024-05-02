package org.javaacademy.dictionary.repository;

import lombok.SneakyThrows;
import org.javaacademy.dictionary.entity.Word;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static java.lang.Thread.sleep;

@Component
public class WordRepository {
    private final TreeMap<String, Word> data = new TreeMap<>();

    @SneakyThrows
    public void add(Word word) {
        //sleep(3000);
        if (data.containsKey(word.getName())) {
            throw new RuntimeException("Уже существует такое слово");
        }
        data.put(word.getName(), word);
    }

    @SneakyThrows
    public Optional<Word> getWord(String key) {
        sleep(3000);
        return Optional.ofNullable(data.get(key));
    }

    @SneakyThrows
    public boolean deleteByKey(String key) {
        sleep(3000);
        return data.remove(key) != null;
    }

    @SneakyThrows
    public List<Word> findAll(int startIndex, int portionSize) {
        sleep(3000);
        return new ArrayList<>(
                data.values().stream()
                .skip(startIndex)
                .limit(portionSize)
                .toList()
        );
    }

    @SneakyThrows
    public Integer getCountEntities() {
        return data.size();
    }

    public void clear() {
        data.clear();
    }
}
