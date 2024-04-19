package wendydeluca.GestionEventi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wendydeluca.GestionEventi.entities.Event;
import wendydeluca.GestionEventi.entities.User;
import wendydeluca.GestionEventi.exceptions.BadRequestException;
import wendydeluca.GestionEventi.payloads.event.EventDTO;
import wendydeluca.GestionEventi.service.EventService;

import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public Page<Event> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "name") String sortBy){
        return eventService.findAllEvents(page,size,sortBy);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Event createEvent(@RequestBody  @Validated EventDTO body, BindingResult validation){
        if(validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        }else{
            return eventService.saveEvent(body);
        }


    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public Event updateEvent(@PathVariable UUID eventId ,@RequestBody  @Validated EventDTO body, BindingResult validation){
        if(validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        }else{
            return eventService.findByIdAndUpdate(eventId,body);
        }


    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public void deleteEvent(@PathVariable UUID eventId){
        eventService.deleteEvent(eventId);


    }

}
