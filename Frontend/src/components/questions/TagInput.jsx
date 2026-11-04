import {useState} from "react";
import {Form, ListGroup,Badge} from "react-bootstrap";
import {suggestTags} from "../../services/tagService.js";

function TagInput({selectedTags,setSelectedTags})
{
    const [input, setInput]= useState("");
    const [suggestions,setSuggestions]=useState([]);
    const  handleChange = async (value)=> {
        setInput(value);

        if (value.length < 2)
        {
            setSuggestions([]);
            return;
        }
        const data = await suggestTags(value);
        setSuggestions(data);
    };

    const addTag = (tag) => {
        if (!selectedTags.some(t=> t.id === tag.id)) {
            setSelectedTags([...selectedTags, tag]);
        }
        setInput("");
        setSuggestions([]);
    };

    const removeTag=(id)=>{ setSelectedTags(selectedTags.filter(tag=>tag.id !==id));
    };


    return (
        <div>
            <Form.Control
                type="text"
                value={input}
                onChange={(e) => handleChange(e.target.value)}
                placeholder="Start typing..."
                autoComplete="off"
                data-cy="tag-input"
            />

            { suggestions.length >0 && (
            <ListGroup className="mt-1 shadow-sm">
                {suggestions.map(tag => (
                    <ListGroup.Item key={tag.id}
                                    data-cy="tag-suggestion"
                                    action
                                    onClick={(e) => { e.preventDefault(); addTag(tag); }}
                                    style={{cursor:"pointer"}}
                    >
                        {tag.name}
                    </ListGroup.Item>
                ))}
            </ListGroup>
                )}

            {selectedTags.length > 0 && (
                <div className="mt-2 d-flex flex-wrap gap-2">
                    {selectedTags.map(tag => (
                        <Badge  bg="secondary" key={tag.id} >
                            <span data-cy="remove-tag" onClick={()=>removeTag(tag.id)}  style={{ cursor: "pointer", fontWeight: "bold" }} > X </span>
                            {tag.name}

                        </Badge>
                    ))}
                </div>
            )}
        </div>
    );
}
export default TagInput;