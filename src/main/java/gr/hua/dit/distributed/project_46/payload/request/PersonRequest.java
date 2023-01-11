package gr.hua.dit.distributed.project_46.payload.request;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PersonRequest {

    @NotBlank(message="Please enter the first name")
    @Size(min = 2, max = 30, message = "First name should not be greater than 30 characters")
    private String firstName;

    @NotBlank(message="Please enter the last name")
    @Size(min = 2, max = 30, message = "Last name should not be greater than 30 characters")
    private String lastName;

    @Size(max = 50, message = "Address should not be greater than 50 characters")
    private String address;

    @NotBlank(message="Please enter the D.O.Y.")
    @Size(max = 30, message = "D.O.Y. should not be greater than 30 characters")
    private String doy;

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

}
