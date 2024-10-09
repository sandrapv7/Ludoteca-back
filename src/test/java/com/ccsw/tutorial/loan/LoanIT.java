package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.model.ClientsDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    private static final String CLIENT_ID_PARAM = "idClient";

    private static final String GAME_ID_PARAM = "idGame";

    private static final String DATE_PARAM = "date";

    private static final int TOTAL_LOANS = 6;

    private static final int PAGE_SIZE = 5;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private LoanService loanService;

    ParameterizedTypeReference<ResponsePage<LoanDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LoanDto>>() {
    };

    private String getUrlWithParams() {
        return UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(GAME_ID_PARAM, "{" + GAME_ID_PARAM + "}").queryParam(CLIENT_ID_PARAM, "{" + CLIENT_ID_PARAM + "}").queryParam(DATE_PARAM, "{" + DATE_PARAM + "}")
                .encode().toUriString();
    }

    @Test
    public void findWithoutFiltersShouldReturnAllLoansInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, 5)); // Usa una página válida

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(CLIENT_ID_PARAM, null);
        params.put(DATE_PARAM, null);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());

    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(1, 5)); // Usa una página válida

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(CLIENT_ID_PARAM, null);
        params.put(DATE_PARAM, null);
        int elementsCount = TOTAL_LOANS - PAGE_SIZE;

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void findWithGameShouldReturnGamesInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, 5)); // Usa una página válida

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, 1L);
        params.put(CLIENT_ID_PARAM, null);
        params.put(DATE_PARAM, null);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);
        assertNotNull(response);
        assertEquals(3, response.getBody().getContent().size());
    }

    @Test
    public void findWithClientsShouldReturnClientsInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, 5)); // Usa una página válida

        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(CLIENT_ID_PARAM, 1L);
        params.put(DATE_PARAM, null);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);
        assertNotNull(response);
        assertNotNull(response.getBody());
        System.out.println(response.getBody());
        assertEquals(2, response.getBody().getNumberOfElements());
    }

    @Test
    public void findWithDateShouldReturnGamesInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));
        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, null);
        params.put(CLIENT_ID_PARAM, null);
        params.put(DATE_PARAM, DATE_EXISTS);

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage, params);
        assertNotNull(response);
        assertEquals(2, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void saveLoanShouldCreateNewLoan() throws Exception {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));
        Map<String, Object> params = new HashMap<>();
        params.put(GAME_ID_PARAM, 2L);
        params.put(CLIENT_ID_PARAM, 3L);
        params.put(DATE_PARAM, null);
        // Crear un LoanDto simulado
        LoanDto dto = new LoanDto();

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setId(1L);
        dto.setClient(clientsDto);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 10);
        dto.setDateStart(new Date());
        dto.setDateEnd(calendar.getTime());

        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);

        assertNotNull(response);
        assertEquals(0, response.getBody().size());

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        response = restTemplate.exchange(getUrlWithParams(), HttpMethod.GET, null, responseType, params);
        assertNotNull(response);
        assertEquals(1, response.getBody().size()); // Asegurarse de que se ha creado un préstamo
    }

    ParameterizedTypeReference<List<LoanDto>> responseType = new ParameterizedTypeReference<List<LoanDto>>() {
    };

    @Test
    public void deleteWithExistsIdShouldDeleteLoan() {
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + 6, HttpMethod.DELETE, null, Void.class);
        ResponseEntity<List<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.DELETE, null, responseType);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().size());
    }

    @Test
    public void deleteWithNotExistsIdShouldInternalError() {
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + 8, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}