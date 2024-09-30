package rj.com.store.enities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Role {
    @Id
    private String roleId;
    private String roleName;
    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> user=new ArrayList<>();
}
