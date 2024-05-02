package by.bsuir.discussionservice.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum Operation {
    CREATE("create"),
    GET_ALL("get_all"),
    GET_BY_STORY_ID("get_by_story_id"),
    GET_BY_ID("get_by_id"),
    UPDATE("update"),
    DELETE("delete");

    private final String operationName;
}
