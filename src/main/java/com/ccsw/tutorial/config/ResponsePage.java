package com.ccsw.tutorial.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Personalizar la respuesta de paginación que se devuelve desde los controladores.
 * @param <T>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePage<T> extends PageImpl<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Se utiliza para deserializar el Json de entrada y convertirlo en un responsepage.
     * @param content
     * @param number
     * @param size
     * @param totalElements
     * @param pageable
     * @param last
     * @param totalPages
     * @param sort
     * @param first
     * @param numberOfElements
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResponsePage(@JsonProperty("content") List<T> content, @JsonProperty("number") int number, @JsonProperty("size") int size, @JsonProperty("totalElements") Long totalElements, @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") boolean last, @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort, @JsonProperty("first") boolean first, @JsonProperty("numberOfElements") int numberOfElements) {


        super(content, PageRequest.of(number, size), totalElements);
    }

    public ResponsePage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public ResponsePage(List<T> content) {
        super(content);
    }

    public ResponsePage() {
        super(new ArrayList<>());
    }

}