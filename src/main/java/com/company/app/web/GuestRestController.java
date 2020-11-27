package com.company.app.web;

import com.company.app.data.entity.Guest;
import com.company.app.data.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rest/guests")
public class GuestRestController {
    @Autowired
    private GuestRepository guestRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(@RequestParam(required = false) Long guestId,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String phonenumber,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Guest> guestList;

        if(Stream.of(guestId, email, phonenumber).allMatch(Objects::isNull)){
            guestList = guestRepository.findAll(paging);
        } else {
            Guest guest = new Guest();
            Optional.ofNullable(guestId).ifPresent(guest::setGuestId);
            guest.setEmailAddress(email);
            guest.setPhoneNumber(phonenumber);
            Example<Guest> example = Example.of(guest);
            guestList = guestRepository.findAll(example, paging);
        }

        if (guestList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("guests", guestList);
        response.put("currentPage", guestList.getNumber());
        response.put("totalItems", guestList.getTotalElements());
        response.put("totalPages", guestList.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
