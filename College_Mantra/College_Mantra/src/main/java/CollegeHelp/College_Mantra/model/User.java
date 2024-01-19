package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.enums.Authority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails , Serializable {

    private static final String AUTHORITY_DELIMITER = ":";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Student student;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private RoomOwner roomOwner;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private ServiceProvider serviceProvider;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Mess mess;

    @OneToMany(mappedBy = "user2")
    @JsonIgnore
    private List<Reputation> reputationsGot;

    @JsonIgnore
    @OneToMany(mappedBy = "user1")
    private List<Reputation> reputationsGave;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String[] authorities = this.authority.toString().split(AUTHORITY_DELIMITER);
        return Arrays
                .stream(authorities)
                .map(x -> new SimpleGrantedAuthority(x))
                .collect(Collectors.toList());

    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
