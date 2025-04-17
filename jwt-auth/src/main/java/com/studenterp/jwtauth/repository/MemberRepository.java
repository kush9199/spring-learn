package com.studenterp.jwtauth.repository;

import com.studenterp.jwtauth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findMemberByUsername(String username);

    boolean existsMemberByUsername(String username);
}
