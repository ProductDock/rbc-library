package com.productdock.service;

import com.productdock.model.Member;

import java.util.List;

public interface MemberService {

    Member save(Member member);

    List<Member> getAll();
}
