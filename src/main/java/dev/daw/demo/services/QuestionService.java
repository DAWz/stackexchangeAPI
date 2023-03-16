package dev.daw.demo.services;

import dev.daw.demo.repositories.QuestionRepo;
import dev.daw.demo.models.Question;
import dev.daw.demo.models.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static dev.daw.demo.models.Question.formatQuestion;

@Service
public class QuestionService {
    @Autowired
    QuestionRepo questionRepo;

    public List<QuestionDTO> findAll(){
        List<Question> questionList = questionRepo.findAll();
        List<QuestionDTO> questionDTOList = questionList.stream().map(Question::formatQuestion).collect(Collectors.toList());
        return questionDTOList;
    }
    public QuestionDTO findQuestion(int userID){
        Question question = questionRepo.findById(userID).orElse(null);
        return formatQuestion(question);
    }
    public Integer deleteQuestion(int userID){
        final Question question =  questionRepo.findById(userID)
                .orElse(null);
        if(question == null){
            return null;
        }
        questionRepo.deleteById(userID);
        return question.getId();
    }

    public List<QuestionDTO> findByTags(List<String> tags, int size, Boolean operation){
        List<Question> questionList = operation ? questionRepo.findByAllTags(tags, size) : questionRepo.findByTagsIn(tags);
        List<QuestionDTO> questionDTOList =  questionList.stream().map(Question::formatQuestion).collect(Collectors.toList());
        return questionDTOList;

    }



}
