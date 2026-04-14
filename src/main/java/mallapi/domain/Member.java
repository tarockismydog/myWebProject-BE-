package mallapi.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Id;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// @Entity가 붙어 있으면 JPA가 테이블을 자동으로 생성
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")
public class Member {

    @Id
    private String email;

    private String pw;

    private String nickname;

    private boolean social;

    // 👉 엔티티가 아닌 값 타입 컬렉션을 DB에 저장할 때 사용
    // 엔티티 X (id 없음) → 단순 값 (String, enum 등)
    // (fetch = FetchType.LAZY) : member 조회할 때 role은 바로 안 가져오고 사용할 때 조회함
    @ElementCollection(fetch = FetchType.LAZY)
    // Builder로 객체 생성할 때 값을 따로 안 넣으면 → 자동으로 new ArrayList<>()
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }

    public void clearRole() {
        memberRoleList.clear();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePw(String pw) {
        this.pw = pw;
    }

    public void changeSocial(boolean social) {
        this.social = social;
     }
}
