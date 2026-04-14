package mallapi.security;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mallapi.domain.Member;
import mallapi.dto.MemberDTO;
import mallapi.repository.MemberRepository;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
    
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        
        log.info("-----------------loadUserByUserName----------------"+ username);

        Member member = memberRepository.getWithRole(username);

        if(member == null) {
            throw new UsernameNotFoundException("NOT FOUND");
        }

        MemberDTO memberDTO = new MemberDTO(
            member.getEmail(),
            member.getPw(),
            member.getNickname(),
            member.isSocial(),
            member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));
        
        return memberDTO;
    }
}

