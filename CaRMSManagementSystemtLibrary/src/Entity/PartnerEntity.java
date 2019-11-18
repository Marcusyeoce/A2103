package Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class PartnerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    
    @OneToMany//(mappedBy = "")
    private List<CustomerEntity> customerEntitys;
    
    @OneToMany//(mappedBy = "")
    private List<ReservationEntity> reservationEntitys;

    public PartnerEntity() {
    }

    public PartnerEntity(String name, String username, String password) {
        this();
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @XmlTransient
    public List<CustomerEntity> getCustomerEntitys() {
        return customerEntitys;
    }

    public void setCustomerEntitys(List<CustomerEntity> customerEntitys) {
        this.customerEntitys = customerEntitys;
    }

    @XmlTransient
    public List<ReservationEntity> getReservationEntitys() {
        return reservationEntitys;
    }

    public void setReservationEntitys(List<ReservationEntity> reservationEntitys) {
        this.reservationEntitys = reservationEntitys;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof PartnerEntity)) {
            return false;
        }
        PartnerEntity other = (PartnerEntity) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.PartnerEntity[ id=" + partnerId + " ]";
    }
    
}
