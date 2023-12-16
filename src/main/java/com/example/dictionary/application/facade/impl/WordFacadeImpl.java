package com.example.dictionary.application.facade.impl;

import com.example.dictionary.application.annotation.ContributeByUser;
import com.example.dictionary.application.dto.CategoryDto;
import com.example.dictionary.application.dto.DefinitionDto;
import com.example.dictionary.application.dto.ExampleDto;
import com.example.dictionary.application.dto.WordDto;
import com.example.dictionary.application.exception.ResourceNotFoundException;
import com.example.dictionary.application.facade.WordFacade;
import com.example.dictionary.application.mapper.CategoryMapper;
import com.example.dictionary.application.mapper.DefinitionMapper;
import com.example.dictionary.application.mapper.ExampleMapper;
import com.example.dictionary.application.mapper.WordMapper;
import com.example.dictionary.application.validator.WordValidator;
import com.example.dictionary.domain.entity.Category;
import com.example.dictionary.domain.entity.Definition;
import com.example.dictionary.domain.entity.Example;
import com.example.dictionary.domain.entity.Word;
import com.example.dictionary.domain.service.CategoryService;
import com.example.dictionary.domain.service.DefinitionService;
import com.example.dictionary.domain.service.ExampleService;
import com.example.dictionary.domain.service.WordService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class WordFacadeImpl implements WordFacade {

    private final WordService wordService;

    private final WordMapper wordMapper;

    private final WordValidator validator;

    private final CategoryService categoryService;

    private final DefinitionService definitionService;

    private final ExampleService exampleService;

    private final CategoryMapper categoryMapper;

    private final DefinitionMapper definitionMapper;

    private final ExampleMapper exampleMapper;

    public WordFacadeImpl(WordService wordService,
                          WordMapper wordMapper,
                          WordValidator validator,
                          CategoryService categoryService,
                          DefinitionService definitionService,
                          ExampleService exampleService,
                          CategoryMapper categoryMapper,
                          DefinitionMapper definitionMapper,
                          ExampleMapper exampleMapper) {
        this.wordService = wordService;
        this.wordMapper = wordMapper;
        this.validator = validator;
        this.categoryService = categoryService;
        this.definitionService = definitionService;
        this.exampleService = exampleService;
        this.categoryMapper = categoryMapper;
        this.definitionMapper = definitionMapper;
        this.exampleMapper = exampleMapper;
    }

    @Override
    public List<WordDto> getAllWords() {
        List<Word> allWords = wordService.getAllWords();
        return allWords.stream()
                .map(wordMapper::wordToWordDto)
                .toList();
    }

    @Override
    public WordDto getWordByName(String name) {
        Word word = wordService.getWordByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Word %s not found".formatted(name)));

        return wordMapper.wordToWordDto(word);
    }

    @Override
    @ContributeByUser
    public WordDto addWord(WordDto wordDto) {
        validator.validate(wordDto);

        Word word = wordMapper.wordDtoToWord(wordDto);
        addToCategory(word);
        addToDefinitions(word);
        addToExamples(word);

        Word addedWord = wordService.addWord(word).orElseThrow();

        return wordMapper.wordToWordDto(addedWord);
    }

    @Override
    public void deleteWordByName(String name) {
        Word wordToDelete = wordService.getWordByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Word %s not found".formatted(name)));

        decoupleFromSynonyms(wordToDelete);

        wordService.deleteWordByName(name);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return categories.stream()
                .map(categoryMapper::categoryToCategoryDto)
                .toList();
    }

    @Override
    public List<DefinitionDto> getAllDefinitions() {
        List<Definition> definitions = definitionService.getAllDefinitions();
        return definitions.stream()
                .map(definitionMapper::definitionToDefinitionDto)
                .toList();
    }

    @Override
    public List<ExampleDto> getAllExamples() {
        List<Example> examples = exampleService.getAllExamples();
        return examples.stream()
                .map(exampleMapper::exampleToExampleDto)
                .toList();
    }

    private static void decoupleFromSynonyms(Word wordToDelete) {
        wordToDelete.getSynonyms().forEach(
                synonym -> {
                    synonym.removeSynonym(wordToDelete);
                    wordToDelete.removeSynonym(synonym);
                }
        );
    }

    private void addToDefinitions(Word word) {
        Set<Definition> definitions = word.getDefinitions();

        definitions.forEach(
                definition ->
                        definitionService
                                .getDefinitionByText(definition.getText())
                                .ifPresent(value -> {
                                    word.removeDefinition(definition);
                                    word.addDefinition(value);
                                })
        );
    }

    private void addToCategory(Word word) {
        categoryService
                .getCategoryByName(word.getCategory().getName())
                .ifPresent(word::setCategory);
    }

    private void addToExamples(Word word) {
        Set<Example> examples = word.getExamples();

        examples.forEach(
                example ->
                        exampleService
                                .getExampleByText(example.getText())
                                .ifPresent(value -> {
                                    word.removeExample(example);
                                    word.addExample(value);
                                })
        );
    }
}
