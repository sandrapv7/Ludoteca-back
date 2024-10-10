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

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    private static final String CLIENT_ID_PARAM = "idClient";

    private static final String GAME_ID_PARAM = "idGame";

    private static final int TOTAL_LOANS = 6;

    private static final int PAGE_SIZE = 5;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Mock
    private LoanService loanService;

    ParameterizedTypeReference<ResponsePage<LoanDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LoanDto>>() {
    };

    @Test
    public void findWithoutFiltersShouldReturnAllLoansInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, 5));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());

    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResult() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(1, 5));

        int elementsCount = TOTAL_LOANS - PAGE_SIZE;

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void findWithGameShouldReturnGamesInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, 5));
        String urlWithParams = UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(GAME_ID_PARAM, 1L).toUriString();
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertEquals(3, response.getBody().getContent().size());
    }

    @Test
    public void findWithClientsShouldReturnClientsInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, 5));
        String urlWithParams = UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(CLIENT_ID_PARAM, 1L).toUriString();
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getNumberOfElements());

    }

    @Test
    public void findWithDateShouldReturnLoansInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE)); // Asegúrate de que PAGE_SIZE es válido
        String urlWithParams = LOCALHOST + port + SERVICE_PATH + "?date=Wed Oct 03 2024";
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, new HttpEntity<>(searchDto), // Enviando el cuerpo de la solicitud
                responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    public void findWithGameAndClientShouldReturnLoansInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));
        String urlWithParams = UriComponentsBuilder.fromHttpUrl(LOCALHOST + port + SERVICE_PATH).queryParam(GAME_ID_PARAM, 1L).queryParam(CLIENT_ID_PARAM, 1L).toUriString();
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    public void findWithGameAndDateShouldReturnLoansInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));
        String urlWithParams = LOCALHOST + port + SERVICE_PATH + "?idGame=1&date=Wed Oct 03 2024";
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, new HttpEntity<>(searchDto), // Enviando el cuerpo de la solicitud
                responseTypePage);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    public void findWithClientAndDateShouldReturnLoansInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));
        String urlWithParams = LOCALHOST + port + SERVICE_PATH + "?idClient=1&date=Wed Oct 03 2024";
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, new HttpEntity<>(searchDto), // Enviando el cuerpo de la solicitud
                responseTypePage);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    public void findWithGameAndClientAndDateShouldReturnLoansInPage() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));
        String urlWithParams = LOCALHOST + port + SERVICE_PATH + "?idGame=1&idClient=1&date=Wed Oct 03 2024";
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(urlWithParams, HttpMethod.POST, new HttpEntity<>(searchDto), // Enviando el cuerpo de la solicitud
                responseTypePage);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    public void saveLoanShouldCreateNewLoan() throws Exception {
        LoanDto loanDto = new LoanDto();

        ClientsDto clientDto = new ClientsDto();
        clientDto.setId(3L);
        loanDto.setClient(clientDto);

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        loanDto.setGame(gameDto);

        Date ini = format.parse("2023-01-03");
        Date fin = format.parse("2023-01-10");

        loanDto.setDateStart(ini);
        loanDto.setDateEnd(fin);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto), Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(TOTAL_LOANS + 1, response.getBody().getTotalElements());
    }

    @Test
    public void saveWithDateEndBeforeDateIniShouldBadRequest() throws Exception {
        LoanDto loanDto = new LoanDto();

        ClientsDto clientDto = new ClientsDto();
        clientDto.setId(3L);
        loanDto.setClient(clientDto);

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        loanDto.setGame(gameDto);

        Date ini = format.parse("2023-01-10");
        Date fin = format.parse("2023-01-03");

        loanDto.setDateStart(ini);
        loanDto.setDateEnd(fin);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveWithPeriodMore14DaysShouldBadRequest() throws Exception {
        LoanDto loanDto = new LoanDto();

        ClientsDto clientDto = new ClientsDto();
        clientDto.setId(3L);
        loanDto.setClient(clientDto);

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        loanDto.setGame(gameDto);

        Date ini = format.parse("2023-01-03");
        Date fin = format.parse("2023-01-18");

        loanDto.setDateStart(ini);
        loanDto.setDateEnd(fin);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void save1Game2TimesSamePeriod() throws Exception {
        LoanDto loanDto = new LoanDto();

        ClientsDto clientDto = new ClientsDto();
        clientDto.setId(3L);
        loanDto.setClient(clientDto);

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        loanDto.setGame(gameDto);

        Date ini = format.parse("2023-01-03");
        Date fin = format.parse("2023-01-10");
        loanDto.setDateStart(ini);
        loanDto.setDateEnd(fin);

        ResponseEntity<?> response1 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto), Void.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        LoanDto loanDto2 = new LoanDto();

        ClientsDto clientDto2 = new ClientsDto();
        clientDto2.setId(2L);
        loanDto2.setClient(clientDto2);

        GameDto gameDto2 = new GameDto();
        gameDto2.setId(1L);
        loanDto2.setGame(gameDto2);

        Date ini2 = format.parse("2023-01-03");
        Date fin2 = format.parse("2023-01-10");
        loanDto2.setDateStart(ini2);
        loanDto2.setDateEnd(fin2);

        ResponseEntity<Void> response2 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto2), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());

    }

    @Test
    public void saveWithMoreThan2LoansClientShouldBadRequest() throws Exception {
        LoanDto loanDto = new LoanDto();

        ClientsDto clientDto = new ClientsDto();
        clientDto.setId(3L);
        loanDto.setClient(clientDto);

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        loanDto.setGame(gameDto);

        Date ini = format.parse("2023-01-03");
        Date fin = format.parse("2023-01-10");

        loanDto.setDateStart(ini);
        loanDto.setDateEnd(fin);

        ResponseEntity<?> response1 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto), Void.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        LoanDto loanDto2 = new LoanDto();

        ClientsDto clientDto2 = new ClientsDto();
        clientDto2.setId(3L);
        loanDto2.setClient(clientDto2);

        GameDto gameDto2 = new GameDto();
        gameDto2.setId(2L);
        loanDto2.setGame(gameDto2);

        Date ini2 = format.parse("2023-01-03");
        Date fin2 = format.parse("2023-01-10");

        loanDto2.setDateStart(ini2);
        loanDto2.setDateEnd(fin2);

        ResponseEntity<?> response2 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto2), Void.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        LoanDto loanDto3 = new LoanDto();

        ClientsDto clientDto3 = new ClientsDto();
        clientDto3.setId(3L);
        loanDto3.setClient(clientDto3);

        GameDto gameDto3 = new GameDto();
        gameDto3.setId(3L);
        loanDto3.setGame(gameDto3);

        Date ini3 = format.parse("2023-01-03");
        Date fin3 = format.parse("2023-01-10");

        loanDto3.setDateStart(ini3);
        loanDto3.setDateEnd(fin3);

        ResponseEntity<?> response3 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto3), Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response3.getStatusCode());

    }

    @Test
    public void deleteWithExistsIdShouldDeleteLoan() {
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + 6L, HttpMethod.DELETE, null, Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, 5));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(TOTAL_LOANS - 1, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void deleteWithNotExistsIdShouldInternalError() {
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + 8, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}