package com.company.app.business.service;

import com.company.app.data.entity.Guest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Instant;
import java.util.*;

@Service
public class IternaryService {

    public Map<Guest,String> generateInternaryNumber(List<Guest> guestList) {
        Map<Guest,String> iternaryMap= new HashMap<>();
        Optional<List<Guest>> optionalList = Optional.ofNullable(guestList);
        //Checks the Optional if list exists, then iterates for each guest, creates the salt and puts to the map
        optionalList.ifPresent(guests -> guests.forEach(guest->iternaryMap.put(guest,createSalt(guest.getGuestId()))));
        return iternaryMap;
    }

    private String createSalt(Long id) {
        long epochSecond = Instant.now().getEpochSecond();
        return DigestUtils.md5DigestAsHex(Long.toString(epochSecond+id).getBytes());
    }
}
