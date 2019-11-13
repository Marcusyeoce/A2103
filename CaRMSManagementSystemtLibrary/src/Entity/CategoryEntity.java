package Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;   
    private String categoryName;
    //private int capacity; 
    
    @OneToMany
    @JoinColumn(nullable = true)
    private List<ModelEntity> models;
    @OneToMany
    @JoinColumn(nullable = true)
    private List<RentalRateEntity> rentalRates;
    @OneToMany
    @JoinColumn(nullable = true)
    private List<ReservationEntity> reservations;
    
    public CategoryEntity() {
    }
    
    public CategoryEntity(String categoryName) {
        this();
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the categoryId fields are not set
        if (!(object instanceof CategoryEntity)) {
            return false;
        }
        CategoryEntity other = (CategoryEntity) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.CategoryEntity[ id=" + categoryId + " ]";
    }
    
}
