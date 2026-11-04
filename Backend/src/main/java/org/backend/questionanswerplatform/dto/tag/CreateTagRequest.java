package org.backend.questionanswerplatform.dto.tag;

import jakarta.validation.constraints.NotBlank;

public class CreateTagRequest {

    @NotBlank(message = "Tag name is required")
    private String name;


    public String getName()
    {return name;}

    public void setName(String name){this.name=name;}


}
