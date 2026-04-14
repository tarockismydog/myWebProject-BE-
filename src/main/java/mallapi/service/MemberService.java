package mallapi.service;

import java.util.stream.Collectors;

import mallapi.domain.Member;
import mallapi.dto.MemberDTO;
import mallapi.dto.MemberModifyDTO;

public interface MemberService {

    MemberDTO getKakaoMember(String accessToken);

    void modifyMember(MemberModifyDTO memberModifyDTO);

    default MemberDTO entityToDTO(Member member) {
        MemberDTO dto = new MemberDTO(
        member.getEmail(), 
        member.getPw(), 
        member.getNickname(), 
        member.isSocial(), 
        member.getMemberRoleList().stream()
                                .map(memberRole -> memberRole.name()).collect(Collectors.toList()));
        return dto;
    }

}
