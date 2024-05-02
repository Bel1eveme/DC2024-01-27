package org.example.publisher.impl.editor.mapper;

import org.example.publisher.impl.editor.Editor;
import org.example.publisher.impl.editor.dto.EditorRequestTo;
import org.example.publisher.impl.editor.dto.EditorResponseTo;

import java.util.List;

public interface EditorMapper {

    EditorRequestTo editorToRequestTo(Editor editor);

    List<EditorRequestTo> editorToRequestTo(Iterable<Editor> editors);

    Editor dtoToEntity(EditorRequestTo editorRequestTo);

    List<Editor> dtoToEntity(Iterable<EditorRequestTo> editorRequestTos);

    EditorResponseTo editorToResponseTo(Editor editor);

    List<EditorResponseTo> editorToResponseTo(Iterable<Editor> editors);
    
    
}
