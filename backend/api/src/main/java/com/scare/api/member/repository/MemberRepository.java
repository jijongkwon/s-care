package com.scare.api.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scare.api.member.domain.Member;
import com.scare.api.member.domain.Provider;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmailAndProvider(String email, Provider provider);

}
