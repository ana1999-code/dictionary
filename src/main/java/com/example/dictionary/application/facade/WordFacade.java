package com.example.dictionary.application.facade;

import com.example.dictionary.application.dto.CommentDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.util.List;
import java.util.Set;

public interface WordFacade {

    List<WordDto> getAllWords();

    WordDto getWordByName(String name);

    WordDto addWord(WordDto wordDto);

    void deleteWordByName(String name);

    WordDto addDefinitionToWord(String name, DefinitionDto definitionDto);

    WordDto removeDefinitionFromWord(String name, DefinitionDto definitionDto);

    WordDto addExampleToWord(String name, ExampleDto exampleDto);

    WordDto removeExampleFromWord(String name, ExampleDto exampleDto);

    Set<WordDto> getAllSynonyms(String name);

    Set<WordDto> getAllAntonyms(String name);

    void addSynonym(String name, WordDto synonym);

    void removeSynonym(String name, WordDto synonym);

    void addAntonym(String name, WordDto antonym);

    void removeAntonym(String name, WordDto antonym);

    void addComment(String name, CommentDto commentDto);

    void removeComment(String name, CommentDto commentDto);

    void uploadFile(String path) throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException;
}
