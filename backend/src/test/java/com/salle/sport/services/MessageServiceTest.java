package com.salle.sport.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageServiceTest {

    @Test
    public void conversationTopicShouldSortIds() {
        MessageService svc = new MessageService();
        String t1 = svc.conversationTopic(5L, 2L);
        String t2 = svc.conversationTopic(2L, 5L);
        assertEquals(t1, t2);
        assertEquals("/topic/conversation.2.5", t1);
    }
}
