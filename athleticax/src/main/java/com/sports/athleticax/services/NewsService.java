@Service
public class NewsService {

    @Value("${news.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getSportsNews() {

        String url =
                "https://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=" + apiKey;

        return restTemplate.getForObject(url, String.class);

    }
}
