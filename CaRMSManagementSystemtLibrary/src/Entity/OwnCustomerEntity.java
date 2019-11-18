package Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class OwnCustomerEntity extends CustomerEntity implements Serializable {
    
    @Column(length = 32, nullable = true, unique = true)
    private String username;
    @Column(length = 32, nullable = true)
    private String password;

    public OwnCustomerEntity() {
    }

    public OwnCustomerEntity(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
