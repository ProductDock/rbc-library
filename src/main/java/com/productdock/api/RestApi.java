package com.productdock.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/members")
public class RestApi {

    static class Member {
        public UUID id;
        public String firstName;
        public String lastName;

        public Member() {}

        public Member(UUID id, String firstName, String lastName) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }
    List<Member> members = new ArrayList<Member>() {
        {
            add(new Member(UUID.randomUUID(),"Pera", "Peric"));
            add(new Member(UUID.randomUUID(),"Nikola", "Nikolic"));
        }
    };

    @GetMapping
    @CrossOrigin
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PostMapping
    @CrossOrigin
    public ResponseEntity<?> create(@RequestBody Member member) {
        member.id = UUID.randomUUID();
        members.add(member);
        return new ResponseEntity<>(member.id, HttpStatus.CREATED);
    }
}
