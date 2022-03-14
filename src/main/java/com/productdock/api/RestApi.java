package com.productdock.api;

import com.productdock.model.Member;
import com.productdock.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class RestApi {

    private final MemberService memberService;

    public RestApi(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String get() {
        return "test";
    }

    @PostMapping
    public ResponseEntity<Member> add(@RequestBody Member member) {
        if (member != null){
            return new ResponseEntity<>(memberService.save(member), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
