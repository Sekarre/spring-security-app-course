package guru.sfg.brewery.domain.security;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String permission;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;
}
