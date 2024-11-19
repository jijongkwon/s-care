package com.scare.api.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scare.api.member.domain.Member;
import com.scare.api.member.domain.Provider;

public interface AuthRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmailAndProvider(String email, Provider provider);

}
