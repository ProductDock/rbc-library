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
    public List<Member> getAll(){
        return allMembers;
    }

    @PostMapping
    public Member addMember(@RequestBody Member member){
        if(member != null){
            allMembers.add(member);
            return member;
        }
        return null;
    }
}
