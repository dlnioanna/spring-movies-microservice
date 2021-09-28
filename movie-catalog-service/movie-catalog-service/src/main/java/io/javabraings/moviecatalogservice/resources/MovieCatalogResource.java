package io.javabraings.moviecatalogservice.resources;

import io.javabraings.moviecatalogservice.models.CatalogItem;
import io.javabraings.moviecatalogservice.models.Movie;
import io.javabraings.moviecatalogservice.models.Rating;
import io.javabraings.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient; //gia advanced load balancing as matho to aplo prota.....

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

// ta ekana beans sthn class MovieCatalogServiceApplication
//        WebClient.Builder builder = WebClient.builder();
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getForObject("http://localhost:8082/movies/foo", Movie.class);

        // get all rated movie ids
// for each movie id call movie info service and get details
//        List<Rating> ratings = Arrays.asList(
//                new Rating("1234", 4),
//                new Rating("5678", 3)
//        );

 //       UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+userId, UserRating.class);
        // αφου εγω ενργοποιησει το eureka server δεν βαζω καρφωτα το ονομα του url αλλά το ονομα του service
        UserRating ratings = restTemplate.getForObject("http://movie-data-service/ratingsdata/users/"+userId, UserRating.class);
        return ratings.getUserRating().stream().map(rating -> {
            // sygxrono
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
//asygxrono
//           Movie movie = webClientBuilder.build()
//           .get()// αλλαζει αναλογα με τη μέθοδο get put post κλπ
//           .uri("http://localhost:8082/movies/"+rating.getMovieId())
//           .retrieve()// κάνει το fetch από το uri που έχω βάλει παραπάνω
//           .bodyToMono(Movie.class)//convert σε στιγμιότυπο της κλάσης που βάζω σαν παραμετρο
//                   // το mono ειναι ασυγχρονο κάτι σαν τα future, promises ή κατι τετοιο
//           .block(); // το block το αναγκαζει να περιμένει μέχρι να φερει αποτελεσμα αλλιως θα είναι αδειο το αποτέλεσμα
//
            return new CatalogItem(movie.getName(), "test", rating.getRating());

        }).collect(Collectors.toList());




/**
 The singletonList() method of java.util.Collections class is used to return an
 immutable list containing only the specified object. The returned list is serializable.
 This list will always contain only one element thus the name singleton list.
 When we try to add/remove an element on the returned singleton list, it would give UnsupportedOperationException.
 */
//return Collections.singletonList(new CatalogItem("Transformers", "test", 4));
    }
}
