import com.journal.journalApp.dto.WheatherApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final String weatherURI = "https://api.weatherstack.com/current?access_key=KEY&query=CITY";
    private final String apiKey = "YOUR_API_KEY";  // Replace with your actual API key
    private final RestTemplate defaultClient;

    public WeatherService(RestTemplate defaultClient) {
        this.defaultClient = defaultClient;
    }

    public ResponseEntity<WheatherApiResponseDTO> getWeather(String city) {
        String finalAPI = weatherURI.replace("KEY", apiKey).replace("CITY", city);

        try {
            // Make the GET request to the weather API
            ResponseEntity<WheatherApiResponseDTO> responseEntity = defaultClient.getForEntity(finalAPI, WheatherApiResponseDTO.class);

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
