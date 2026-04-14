package mallapi.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MemberDTO extends User {

    private String email, pw, nickname;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(String email, String pw, String nickname, boolean social, List<String> roleNames) {

        super(
            email, pw, roleNames.stream().map(str->new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList()));
        
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;

    }

    public Map<String, Object> getClaims() {
        Map<String, Object> dateMap = new HashMap<>();

        dateMap.put("email", email);
        dateMap.put("pw", pw);
        dateMap.put("nickname", nickname);
        dateMap.put("social", social);
        dateMap.put("roleNames", roleNames);
    
        return dateMap;
    }

}
