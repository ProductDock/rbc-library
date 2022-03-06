package com.productdock.api;

import com.productdock.model.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/members")
public class RestApi {

    @GetMapping
    public String get() {
        return "test";
    }

    @PostMapping
    public ResponseEntity<Member> add(@RequestBody Member member) {
        if (member != null){
            member.setId(System.currentTimeMillis());
            System.out.println(member);
            return new ResponseEntity<>(member, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
