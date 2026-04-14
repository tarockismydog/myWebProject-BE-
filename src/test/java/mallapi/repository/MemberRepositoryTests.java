package mallapi.repository;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j2;
import mallapi.domain.Member;
import mallapi.domain.MemberRole;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
    // 회원정보등록 시 password는 암호화해서 넣음    
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Disabled
    @Test
    public void testInsertMember() {
        for(int i=0 ; i<10; i++) {
            Member member = Member.builder()
                                .email("user"+i+"@aaa.com")
                                .pw(passwordEncoder.encode("1111"))
                                .nickname("USER"+i)
                                .build();
            
            member.addRole(MemberRole.USER);

            if(i >= 5) {
                member.addRole(MemberRole.MANAGER);
            }

            if(i >= 8) {
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        }
    }

    @Test
    public void testRead() {
        String email = "user9@aaa.com";
        Member member = memberRepository.getWithRole(email);

        log.info("-------------");
        log.info(member);
        log.info(member.getMemberRoleList());

    }
     
}
