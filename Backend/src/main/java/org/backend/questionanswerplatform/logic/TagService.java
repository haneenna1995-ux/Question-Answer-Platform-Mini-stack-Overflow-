package org.backend.questionanswerplatform.logic;

import org.backend.questionanswerplatform.dto.tag.CreateTagRequest;
import org.backend.questionanswerplatform.dto.tag.TagResponse;
import org.backend.questionanswerplatform.entities.Tag;
import org.backend.questionanswerplatform.exception.UnknownTagException;
import org.backend.questionanswerplatform.mappers.TagMapper;
import org.backend.questionanswerplatform.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagService (TagRepository tagRepository, TagMapper tagMapper)
    {
        this.tagRepository= tagRepository;
        this.tagMapper=tagMapper;
    }

    public TagResponse createTag(CreateTagRequest request)
    {
        if(tagRepository.findByNameIgnoreCase(request.getName().trim()).isPresent())
        {throw new IllegalArgumentException("Tag is already exist" +request.getName());}

        Tag tag = new Tag(request.getName().trim().toLowerCase());
        Tag saved= tagRepository.save(tag);

        return tagMapper.toResponse(saved);
    }


    public Set<Tag> resolveTags(List<Long> tagIds) {
        List<Long> uniqueIds = tagIds.stream().distinct().toList();

        List<Tag> found = tagRepository.findAllById(uniqueIds);

        if (found.size() != uniqueIds.size()) {
            List<Long> foundIds = new ArrayList<>();
            for (Tag tag : found) {
                foundIds.add(tag.getId());
            }

            List<Long> missing = new ArrayList<>();
            for (Long id : uniqueIds) {
                if (!foundIds.contains(id)) {
                    missing.add(id);
                }
            }

            throw new UnknownTagException(missing);
        }

        return new HashSet<>(found);
    }

public  List<TagResponse> getSuggestions(String query){
   if (query==null || query.trim().length() <2)
   {return List.of();}
   String clean = query.trim();
    return tagRepository.findByNameContainingIgnoreCase(clean).stream()
            .map(tagMapper::toResponse)
            .toList();
}
}
