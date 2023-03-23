package dev.daw.demo.services;

import dev.daw.demo.models.Tag;
import dev.daw.demo.repositories.TagRepo;
import org.apache.hc.client5.http.auth.AuthStateCacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    TagRepo tagRepo;

    public List<Tag> findAll() {
        return tagRepo.findAll();
    }
}
