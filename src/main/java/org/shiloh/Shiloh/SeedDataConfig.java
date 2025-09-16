package org.shiloh.Shiloh;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SeedDataConfig {

    @Bean
    CommandLineRunner initDatabase(BookRepository repo) {
        return args -> {
            repo.save(new Book(null, "The Pilgrim’s Progress", "John Bunyan", 1678));
            repo.save(new Book(null, "Paradise Lost", "John Milton", 1667));
            repo.save(new Book(null, "Confessions", "St. Augustine", 398));
            repo.save(new Book(null, "City of God", "St. Augustine", 426));
            repo.save(new Book(null, "The Divine Comedy", "Dante Alighieri", 1320));
            repo.save(new Book(null, "Summa Theologica", "Thomas Aquinas", 1274));
            repo.save(new Book(null, "The Imitation of Christ", "Thomas à Kempis", 1418));
            repo.save(new Book(null, "The Practice of the Presence of God", "Brother Lawrence", 1691));
        };
    }
}

