package pdp.pdprest.service;

import org.apache.tomcat.util.buf.StringUtils;
import org.glassfish.jersey.internal.guava.Lists;
import pdp.pdprest.domain.TaskEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskService {
    private Map<Long, TaskEntity> data = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(0);

    private static TaskService instance = new TaskService();

    private TaskService() {
    }

    public static TaskService getInstance() {
        return instance;
    }

    public Collection<TaskEntity> get() {
        return data.values();
    }

    public TaskEntity get(long id) {
        return data.get(id);
    }

    public List<TaskEntity> search(String firstName, String lastName, String sort) {
        return data.values().stream().filter(getSearchPredicate(firstName, lastName)).sorted(getSortComparator(sort)).collect(Collectors.toList());
    }

    private Comparator<TaskEntity> getSortComparator(String sort) {
        if(sort == null) {
            return Comparator.comparingLong(TaskEntity::getId);
        }
        Comparator<TaskEntity> result;

        String[] parts = sort.split(":");
        switch (parts[0]) {
            case "firstName" :
                result = Comparator.comparing(TaskEntity::getFirstName, String::compareToIgnoreCase);
                break;
            case "lastName":
                result = Comparator.comparing(TaskEntity::getFirstName, String::compareToIgnoreCase);
                break;

            default:
                result = Comparator.comparingLong(TaskEntity::getId);
        }

        if ("desc".equalsIgnoreCase(parts[1])) {
            return result.reversed();
        }
        return result;
    }

    private Predicate<TaskEntity> getSearchPredicate(String firstName, String lastName) {
        if(firstName != null && lastName != null) {
            return e -> firstName.equals(e.getFirstName()) && lastName.equals(e.getLastName());
        } else if (firstName != null) {
            return e -> firstName.equals(e.getFirstName());
        } else if(lastName != null) {
            return e -> lastName.equals(e.getLastName());
        } else {
            return e -> true;
        }
    }

    public long add(TaskEntity elem) {
        long id = nextKey();
        elem.setId(id);
        data.putIfAbsent(id, elem);
        return id;
    }

    public void save(TaskEntity elem) {
        data.put(elem.getId(), elem);
    }

    public void delete(Long id) {
        data.remove(id);
    }


    private long nextKey() {
        long id;
        while (true) {
            id = idGenerator.incrementAndGet();
            if (!data.containsKey(id)) {
                break;
            }
        }
        return id;
    }

}
