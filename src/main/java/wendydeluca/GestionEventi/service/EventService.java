package wendydeluca.GestionEventi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wendydeluca.GestionEventi.entities.Event;
import wendydeluca.GestionEventi.entities.User;
import wendydeluca.GestionEventi.exceptions.NotFoundException;
import wendydeluca.GestionEventi.payloads.event.EventDTO;
import wendydeluca.GestionEventi.repositories.EventDAO;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventDAO eventDAO;
    public Page<Event> findAllEvents(int page, int size, String sortBy) {
        if (size < 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventDAO.findAll(pageable);
    }

    public Event findEventById(UUID eventId) {
        return eventDAO.findById(eventId).orElseThrow(()-> new NotFoundException("The event with id " + eventId + "has nor been found!"));

    }

    public Event saveEvent(EventDTO payload){
        Event event = new Event();
        event.setTitle(payload.title());
        event.setDate(LocalDate.parse(payload.date()));
        event.setLocation(payload.location());
        event.setDescription(payload.description());
        event.setAvailableSeats(payload.availableSeats());
        return eventDAO.save(event);

    }

    public Event findByIdAndUpdate(UUID eventId, EventDTO payload){
        Event event = eventDAO.findById(eventId).orElseThrow(()-> new NotFoundException("The event with id " + eventId + "has not been found"));
        event.setTitle(payload.title());
        event.setDate(LocalDate.parse(payload.date()));
        event.setLocation(payload.location());
        event.setDescription(payload.description());
        event.setAvailableSeats(payload.availableSeats());
        return eventDAO.save(event);

    }

    public void deleteEvent(UUID eventId){
        Event event = eventDAO.findById(eventId).orElseThrow(()-> new NotFoundException(eventId));
        eventDAO.delete(event);
    }

}
