package Entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class CarEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    @Column(nullable = false, unique = true, length = 10)
    @NotNull
    private String licensePlateNumber;
    @Column(nullable = false, length = 32)
    @NotNull
    private String color;
    private boolean isAvailable; //status    
    
    @ManyToOne//(optional = true)
    //@JoinColumn(nullable = false)
    private ModelEntity modelEntity;
    @ManyToOne
    private OutletEntity outlet; //location

    public CarEntity() {
    }

    public CarEntity(String licensePlateNumber, String color, ModelEntity modelEntity) {
        this();
        this.licensePlateNumber = licensePlateNumber;
        this.color = color;
        this.modelEntity = modelEntity;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ModelEntity getModelEntity() {
        return modelEntity;
    }

    public void setModelEntity(ModelEntity modelEntity) {
        this.modelEntity = modelEntity;
    }
    
    

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
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
