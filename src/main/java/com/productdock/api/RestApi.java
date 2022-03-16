package com.productdock.api;

import com.productdock.model.Member;
import com.productdock.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class RestApi {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String get() {
        return "test";
    }

    @PostMapping
    public ResponseEntity<Member> add(@Valid @RequestBody Member member) {
        return new ResponseEntity<>(memberService.save(member), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Member>> getAll() {
        return new ResponseEntity(memberService.getAll(), HttpStatus.OK);
    }

}
