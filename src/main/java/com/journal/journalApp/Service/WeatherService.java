package com.journal.journalApp.Service;


import com.journal.journalApp.constants.ApplicationConstants;
import com.journal.journalApp.dto.WheatherApiResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Service

public class WeatherService {

    private final String weatherURI = "http://api.weatherstack.com/current?access_key=KEY&query=CITY";
    private final RestClient restClient;

    public WeatherService(RestClient restClient) {
        this.restClient = restClient;
    }


    public ResponseEntity<WheatherApiResponseDTO> getWeather(String city) {
        String finalAPI = weatherURI.replace("KEY", ApplicationConstants.API_ACCESS_KEY).replace("CITY", city);

        try {
            // Make the GET request to the weather API
            ResponseEntity<WheatherApiResponseDTO> responseEntity = restClient.get()
                    .uri(finalAPI)
                    .retrieve()
                    .toEntity(WheatherApiResponseDTO.class);

            // Check if the response is empty or null
            if (responseEntity == null || responseEntity.getBody() == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            // Return the successful response
            return responseEntity;

        } catch (Exception e) {
            // Log the exception (you can use a logger in production)
            System.err.println("Error while fetching weather data: " + e.getMessage());





            // Return an INTERNAL_SERVER_ERROR status with an error message
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
