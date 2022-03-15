package com.productdock.service;

import com.productdock.model.Member;
import com.productdock.repository.MemberRepository;
import jnr.a64asm.Mem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public List<Member> getAll(){
        return memberRepository.findAll();
    }
}
