package com.company.app.web;

import com.company.app.business.service.IternaryService;
import com.company.app.data.entity.Guest;
import com.company.app.data.repository.GuestRepository;
import com.company.app.data.repository.TicketRepository;
import com.company.app.data.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rest/internary")
public class IternaryRestController {

    @Autowired
    private IternaryService iternaryService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private GuestRepository guestRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(@RequestParam(required = false) Long ticketId,
                                                       @RequestParam(required = false) String iternary,
                                                       @RequestParam(required = false) Long guestId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Ticket> ticketList;

        if (Stream.of(ticketId, iternary, guestId).allMatch(Objects::isNull)) {
            ticketList = ticketRepository.findAll(paging);
        } else {
            Ticket ticket = new Ticket();
            ticket.setTicketId(ticketId);
            ticket.setIternaryString(iternary);

            Guest guest = new Guest();
            guest.setGuestId(guestId);
            ticket.setGuest(guest);

            Example<Ticket> example = Example.of(ticket);
            ticketList = ticketRepository.findAll(example, paging);
        }

        if (ticketList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("tickets", ticketList);
        response.put("currentPage", ticketList.getNumber());
        response.put("totalItems", ticketList.getTotalElements());
        response.put("totalPages", ticketList.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @RequestMapping(path = "/generateIternaries", method = RequestMethod.POST,consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> createTicket(@RequestBody List<Guest> guests){
        List<Optional<Guest>> collect = guests.stream().map(i -> guestRepository.findById(i.getGuestId())).parallel().collect(Collectors.toList());

        List<Guest> dataFilledGuestList= new ArrayList<>();
        collect.forEach(i -> i.ifPresent(dataFilledGuestList::add));

        List<Ticket> ticketList = new ArrayList<>();
        Map<Guest, String> guestStringMap = iternaryService.generateInternaryNumber(dataFilledGuestList);
        guestStringMap.forEach((k,v)-> ticketList.add(ticketRepository.save(createTicket(k,v))));

        if (ticketList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            Map<String, Object> response = new HashMap<>();
            response.put("tickets", ticketList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    private Ticket createTicket(Guest k, String v) {
        Ticket ticket = new Ticket();
        ticket.setGuest(k);
        ticket.setIternaryString(v);
        return ticket;
    }

}
