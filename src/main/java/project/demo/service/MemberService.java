package project.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.demo.entity.Member;
import project.demo.repository.MemberRepository;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public Member getMemberById(int memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }
}
