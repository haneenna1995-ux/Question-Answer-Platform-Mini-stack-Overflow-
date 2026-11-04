package org.backend.questionanswerplatform.mappers;

import org.backend.questionanswerplatform.dto.tag.TagResponse;
import org.backend.questionanswerplatform.entities.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public TagResponse toResponse(Tag t)
    {
        return new TagResponse(t.getId(),t.getName());

    }

}
