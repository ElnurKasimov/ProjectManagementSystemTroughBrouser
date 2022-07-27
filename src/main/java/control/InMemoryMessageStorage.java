package control;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemoryMessageStorage implements MessageStorage {
    private List<Message> messages = new CopyOnWriteArrayList<>();

    /*
    private final static InMemoryMessageStorage INSTANCE = new InMemoryMessageStorage();

    public static InMemoryMessageStorage getInstance() {
        return INSTANCE;
    }

   // private InMemoryMessageStorage() {
    //}

     */

    @Override
    public List<Message> getAllMessages() {
        return messages;
    }

    @Override
    public void add(Message message) {
        messages.add(message);
    }

    @Override
    public void deleteById(String id) {
        messages = messages.stream()
                .filter(it -> !it.getId().equals(id))
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }
}
