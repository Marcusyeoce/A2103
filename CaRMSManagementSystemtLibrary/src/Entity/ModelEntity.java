package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class ModelEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;
    @Column(nullable = false)
    private String make;
    @Column(nullable = false, unique = true)
    private String model;
    @Column(nullable = false)
    private boolean isDeleted = false;
    
    @OneToMany(mappedBy = "modelEntity")
    private List<CarEntity> cars;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private CategoryEntity categoryEntity;
    
    @OneToMany//(mappedBy = "")
    private List<ReservationEntity> reservations;

    public ModelEntity() {
        cars = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public ModelEntity(String make, String model) {
        this();
        this.make = make;
        this.model = model;
    }
    
    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    @XmlTransient
    public List<ReservationEntity> getReservations() {
        return reservations;
    }
    
    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    
    public List<CarEntity> getCars() {
       return cars;
    }

    public void setCars(List<CarEntity> cars) {
        this.cars = cars;
    }

    @XmlTransient
    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modelId != null ? modelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the modelId fields are not set
        if (!(object instanceof ModelEntity)) {
            return false;
        }
        ModelEntity other = (ModelEntity) object;
        if ((this.modelId == null && other.modelId != null) || (this.modelId != null && !this.modelId.equals(other.modelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.ModelEntity[ id=" + modelId + " ]";
    }
}
