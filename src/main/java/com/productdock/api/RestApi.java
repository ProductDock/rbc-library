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

    static class MemberDTO {
        public String firstName;
        public String lastName;

        public MemberDTO() {}

        public MemberDTO(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    static class Member {
        public UUID id;
        public String firstName;
        public String lastName;

        public Member() {}

        public Member(String firstName, String lastName) {
            this.id = UUID.randomUUID();
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }
    List<Member> members = new ArrayList<Member>() {
        {
            add(new Member("Pera", "Peric"));
            add(new Member("Nikola", "Nikolic"));
        }
    };

    @GetMapping
    @CrossOrigin
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @PostMapping
    @CrossOrigin
    public ResponseEntity<?> create(@RequestBody MemberDTO memberDTO) {
        Member mem = new Member(memberDTO.firstName, memberDTO.lastName);
        members.add(mem);
        return new ResponseEntity<>(mem.id, HttpStatus.CREATED);
    }
}
