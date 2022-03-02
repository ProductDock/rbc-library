package com.productdock.rbcprojectstarterdp.api;

import com.productdock.rbcprojectstarterdp.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/members")
public class RestApi {

    private List<Member> allMembers = new ArrayList<Member>();

    @GetMapping
    public ResponseEntity<List<Member>> getAll(){
        return new ResponseEntity(allMembers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Member> addMember(@RequestBody Member member){
        if(member != null){
            allMembers.add(member);
            return new ResponseEntity(member, HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
