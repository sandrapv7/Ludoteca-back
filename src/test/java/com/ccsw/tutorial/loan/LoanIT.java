package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    public static final Long EXISTS_LOAN_ID = 1L;

    public static final Long EXISTS_GAME_ID = 1L;

    public static final Long EXISTS_CLIENT_ID = 1L;

    public static final Date DATE_EXISTS;

    static {
        try {
            DATE_EXISTS = new SimpleDateFormat("yyyy-MM-dd").parse("2024-10-12");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Long NOT_EXISTS_GAME_ID = 0L;

    private static final String CLIENT_ID_PARAM = "idClient";

    private static final String GAME_ID_PARAM = "idGame";

    private static final String DATE_PARAM = "date";

    private static final int TOTAL_LOANS = 6;
    private static final int PAGE_SIZE = 5;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<LoanDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LoanDto>>() {
    };

    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(GAME_ID_PARAM, "{" + GAME_ID_PARAM + "}").queryParam(CLIENT_ID_PARAM, "{" + CLIENT_ID_PARAM + "}").queryParam(DATE_PARAM, "{" + DATE_PARAM + "}")
                .encode().toUriString();
    }

    @Test
    public void findWithoutFiltersShouldReturnAllPrestamosInPage() {
        LoanSearchDto loanDto = new LoanSearchDto();
        loanDto.setPageable(new PageableRequest(0, PAGE_SIZE));
        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(CLIENT_ID_PARAM, null);
        params.put(DATE_PARAM, null);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(loanDto), responseTypePage, params);
        assertNotNull(response);
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

}