package gr.hua.dit.distributed.project_46.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(	name = "persons")

public class Person  {
    @Id
    @Column(name = "tin", length = 9)
    @NotBlank(message="Please enter the Taxpayer Identification Number")
    @Size(min = 9, max = 9, message = "FTaxpayer Identification Number should be 9 characters")
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

    public void setTin(String tin) {
        this.tin = tin;
    }

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
