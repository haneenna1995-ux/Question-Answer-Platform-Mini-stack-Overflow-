package org.backend.questionanswerplatform.logic;

import org.backend.questionanswerplatform.dto.tag.TagResponse;
import org.backend.questionanswerplatform.entities.Tag;
import org.backend.questionanswerplatform.exception.UnknownTagException;
import org.backend.questionanswerplatform.mappers.TagMapper;
import org.backend.questionanswerplatform.repositories.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class TagServiceTest {
    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagService tagService;

    @Test
    void resolveTags_shouldReturnsTagsWhenAllIdsExist()
    {
        //Arrange
        Tag java = new Tag();
        java.setId(1L);
        Tag spring = new Tag();
        spring.setId(2L);

        when(tagRepository.findAllById(List.of(1L,2L))).thenReturn(List.of(java,spring));

        //Act
        Set<Tag> result= tagService.resolveTags(List.of(1L,2L));
        // Assert
        assertThat(result).containsExactlyInAnyOrder(java, spring);
    }

    @Test
    void resolveTags_deduplicatesIdsBeforeQuerying(){

        //Arrange
        Tag java= new Tag();
        java.setId(1L);

        Tag spring = new Tag();
        spring.setId(2L);
        when(tagRepository.findAllById(List.of(1L,2L))).thenReturn(List.of(java,spring));

        //Assert
        tagService.resolveTags(List.of(1L, 1L, 2L));

        //Verify
        verify(tagRepository).findAllById(List.of(1L,2L));

    }

    @Test
    void resolveTags_throwsWhenSomeIdsAreMissing()
    {
        //Arrange
        Tag java= new Tag();
        java.setId(1L);

        when(tagRepository.findAllById(List.of(1L,2L))).thenReturn(List.of(java));

        // assert
        List<Long> tagIds = List.of(1L, 2L);
        assertThatThrownBy(() -> tagService.resolveTags(tagIds))
                .isInstanceOf(UnknownTagException.class);

    }

    @Test
    void getSuggestions_ShouldReturnEmptyWhenQueryTooShort(){
        assertThat(tagService.getSuggestions("a")).isEmpty();
        assertThat(tagService.getSuggestions(" ")).isEmpty();
        assertThat(tagService.getSuggestions("")).isEmpty();
    }

    @Test
    void getSuggestion_ShouldReturnMappedResponsesForValidQuery(){
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("java");

        TagResponse response = new TagResponse(1L, "java");

        when(tagRepository.findByNameContainingIgnoreCase("jav"))
                .thenReturn(List.of(tag));
        when(tagMapper.toResponse(tag)).thenReturn(response);

        List<TagResponse> result = tagService.getSuggestions("jav");

        assertThat(result).containsExactly(response);

    }




}
