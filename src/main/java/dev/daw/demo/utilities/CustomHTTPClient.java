package dev.daw.demo.utilities;

import dev.daw.demo.exceptions.ApplicationException;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomHTTPClient {

    private HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
            HttpClientBuilder.create().build());
    private RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
    public String externalCall(String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON));
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);
        if(response.getStatusCode() != HttpStatus.OK){
            throw new ApplicationException( "server-error",
                    String.format("Failed while calling url: %s", url),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String jsonString = response.getBody();
        return jsonString;
    }
}
