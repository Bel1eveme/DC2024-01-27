package com.example.rv.impl.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EditorRepositoryTest {

    EditorRepository underTestEditorRepository = new EditorRepository();


    @Test
    void checkingOptionalToEditor(){
        Editor respTo = new Editor(-1L, "miki", "123", "me", "me");

        Editor ed = underTestEditorRepository.save(respTo).orElse(null);

        Assertions.assertNotNull(ed);
        Assertions.assertEquals(ed.getId(), respTo.getId());
    }

}