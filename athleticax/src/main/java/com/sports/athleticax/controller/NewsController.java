@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "https://resilient-centaur-8cbadb.netlify.app")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/sports")
    public ResponseEntity<String> sportsNews() {

        return ResponseEntity.ok(newsService.getSportsNews());

    }
}
