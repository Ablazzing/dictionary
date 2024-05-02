package org.javaacademy.dictionary;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.javaacademy.dictionary.entity.Word;
import org.javaacademy.dictionary.repository.WordRepository;
import org.javaacademy.dictionary.service.WordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Slf4j
@RequiredArgsConstructor
public class WordControllerTest {
    private static final String BASE_URL = "/api/dictionary";
    private static final String WORD_KEY = "ball";
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private WordService wordService;

    @BeforeEach
    public void init() {
        wordRepository.clear();
        wordService.clearCache();
    }

    @Test
    @SneakyThrows
    void createWordSuccess() {
        createWord();
        Word word = wordRepository.getWord("ball").orElseThrow();
        Integer countEntities = wordRepository.getCountEntities();
        Assertions.assertEquals("ball", word.getName());
        Assertions.assertEquals(1, countEntities);
    }

    @Test
    void getWordsSuccess() {
        createWord();
        RestAssured
                .given()
                .queryParam("startPosition", 0)
                .queryParam("portionSize", 1)
                .log().all()
                .get(BASE_URL)
                .then()
                .log().all()
                .statusCode(200)
                .body("startPosition", equalTo(0))
                .body("endPosition", equalTo(1))
                .body("totalSize", equalTo(1))
                .body("portionSize", equalTo(1))
                .body("content[0].name", equalTo("ball"))
                .body("content[0].description", equalTo("мяч"));

        RestAssured
                .given()
                .queryParam("startPosition", 0)
                .queryParam("portionSize", 1)
                .get(BASE_URL)
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(1L), SECONDS);
    }

    @SneakyThrows
    private void createWord() {
        String body = readAllLines("create_word.json");
        RestAssured
                .given()
                .body(body)
                .log().all()
                .contentType(ContentType.JSON)
                .post(BASE_URL)
                .then()
                .log().all()
                .statusCode(HttpStatus.ACCEPTED.value());
    }

    @Test
    void deleteSuccess() {
        createWord();
        RestAssured
                .given()
                .log().all()
                .delete(BASE_URL + "/{key}", WORD_KEY)
                .then()
                .log().all()
                .statusCode(202);
        assertTrue(wordRepository.getWord(WORD_KEY).isEmpty());

        RestAssured
                .given()
                .log().all()
                .delete(BASE_URL + "/{key}", WORD_KEY)
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    void getByKeySuccess() {
        createWord();
        RestAssured
                .given()
                .log().all()
                .get(BASE_URL + "/{key}", WORD_KEY)
                .then()
                .statusCode(200)
                .log().all()
                .body("name", equalTo( "ball"))
                .body("description", equalTo("мяч"));
    }

    @SneakyThrows
    private String readAllLines(String filename) {
        URL url = this.getClass().getClassLoader().getResource(filename);

        Scanner scanner = new Scanner(new File(url.toURI()));
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNext()) {
            builder.append(scanner.nextLine())
                    .append("\n");
        }
        return builder.toString();
    }
}
