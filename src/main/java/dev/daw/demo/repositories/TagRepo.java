package dev.daw.demo.repositories;

import dev.daw.demo.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, String> {
}
