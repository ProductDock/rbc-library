package com.productdock.rbcprojectstarterdp.api;

import com.productdock.rbcprojectstarterdp.model.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/members")
public class RestApi {

    public static List<Member> members = new ArrayList<>() {
        {
            add(new Member("Milica", "Milica"));
        }
    };

    @GetMapping
    public List<Member> getAll(){
        return members;
    }

    @PostMapping
    public Member addMember(@RequestBody Member member){
        if(member != null){
            members.add(member);
            return member;
        }
        return null;
    };
}
