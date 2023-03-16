package dev.daw.demo.repositories;

import dev.daw.demo.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {

    List<Question> findByTagsIn(List<String> tags);

    @Query(value = "select t.*\n" +
            "from tag_question tq join\n" +
            "     questions t\n" +
            "     on tq.question_id = t.id\n" +
            "where tq.tag_name in (?1 )\n" +
            "group by t.id\n" +
            "having count(*) = ?2 ;", nativeQuery = true)
    List<Question> findByAllTags(List<String> tags, int tagsNumber);

}
