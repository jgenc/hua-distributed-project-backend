package gr.hua.dit.distributed.project_46.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(	name = "persons")

public class Person  {
    @Id
    @Column(name = "tin", length = 9)
    @NotBlank(message="Please enter the Taxpayer Identification Number")
    @Size(min = 9, max = 9, message = "Taxpayer Identification Number should be 9 characters")
    private String tin;

    @Column(name = "firstname", length = 30)
    @NotBlank(message="Please enter the first name")
    @Size(min = 2, max = 30, message = "First name should not be greater than 30 characters")
    private String firstName;

    @Column(name = "lastname", length = 30)
    @NotBlank(message="Please enter the last name")
    @Size(min = 2, max = 30, message = "Last name should not be greater than 30 characters")
    private String lastName;

    @Column(name = "address", length = 50)
    @Size(max = 50, message = "Address should not be greater than 50 characters")
    private String address;

    @Column(name = "doy", length = 30)
    @NotBlank(message="Please enter the D.O.Y.")
    @Size(max = 30, message = "D.O.Y. should not be greater than 30 characters")
    private String doy;

    @OneToMany(mappedBy = "notary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Declaration> notaryDeclarations;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Declaration> sellerDeclarations;

    @OneToMany(mappedBy = "purchaser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Declaration> purchaserDeclarations;

    public Person() {
    }

    public Person(String tin, String firstName, String lastName, String address, String doy) {
        this.tin = tin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.doy = doy;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) { this.tin = tin; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoy() {
        return doy;
    }

    public void setDoy(String doy) {
        this.doy = doy;
    }

    public Set<Declaration> getNotaryDeclarations() {
        return notaryDeclarations;
    }

    public void setNotaryDeclarations(Set<Declaration> notaryDeclarations) {
        this.notaryDeclarations = notaryDeclarations;
    }

    public Set<Declaration> getSellerDeclarations() {
        return sellerDeclarations;
    }

    public void setSellerDeclarations(Set<Declaration> sellerDeclarations) {
        this.sellerDeclarations = sellerDeclarations;
    }

    public Set<Declaration> getPurchaserDeclarations() {
        return purchaserDeclarations;
    }

    public void setPurchaserDeclarations(Set<Declaration> purchaserDeclarations) {
        this.purchaserDeclarations = purchaserDeclarations;
    }

    @Override
    public String toString() {
        return "User{" +
                ", tin='" + tin + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", doy='" + doy + '\'' +
                '}';
    }

}
