package Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class CarEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @Column(nullable = false, unique = true, length = 15)
    @NotNull
    private String licensePlateNumber;
    @Column(nullable = false, length = 32)
    @NotNull
    private String status; //Available, Repair, Unavailable
    
    @ManyToOne(optional = true) //should be false
    @JoinColumn(nullable = true) 
    private ModelEntity modelEntity;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private OutletEntity outlet; //location
    
    @OneToOne
    private ReservationEntity reservationEntity;

    public CarEntity() {
    }

    public CarEntity(String licensePlateNumber, String status) {
        this();
        this.licensePlateNumber = licensePlateNumber;
        this.status = status;
    }
    
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }
    
    @XmlTransient
    public OutletEntity getOutlet() {
        return outlet;
    }

    public void setOutlet(OutletEntity outlet) {
        this.outlet = outlet;
    }

    @XmlTransient
    public ReservationEntity getReservationEntity() {
        return reservationEntity;
    }

    public void setReservationEntity(ReservationEntity reservationEntity) {
        this.reservationEntity = reservationEntity;
    }
    
    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public void setModelEntity(ModelEntity modelEntity) {
        this.modelEntity = modelEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof CarEntity)) {
            return false;
        }
        CarEntity other = (CarEntity) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.CarEntity[ id=" + carId + " ]";
    }
    
}
