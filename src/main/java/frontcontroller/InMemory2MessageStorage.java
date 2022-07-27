package frontcontroller;

import control.Message;
import control.MessageStorage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemory2MessageStorage implements MessageStorage {
    private List<Message> messages = new CopyOnWriteArrayList<>();

    private final static InMemory2MessageStorage INSTANCE = new InMemory2MessageStorage();

    public static InMemory2MessageStorage getInstance() {
        return INSTANCE;
    }

    private InMemory2MessageStorage() {
    }

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
