package gr.hua.dit.distributed.project_46.payload.request;

import gr.hua.dit.distributed.project_46.entity.ERole;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddDeclarationRequest {

    @NotBlank
    @Size(min = 9, max = 9)
    private String notaryTin;

    @NotBlank
    @Size(min = 1, max = 15)
    private String propertyNumber;

    @NotBlank
    @Size(min = 1, max = 15)
    private String propertyCategory;

    @NotBlank
    @Size(min = 3, max = 100)
    private String propertyDescription;

    @NotBlank
    @Size(min = 9, max = 9)
    private String sellerTin;

    @NotBlank
    @Size(min = 9, max = 9)
    private String purchaserTin;

    private Float tax;

     public String getNotaryTin() {
        return notaryTin;
    }

    public void setNotaryTin(String notaryTin) {
        this.notaryTin = notaryTin;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public String getSellerTin() {
        return sellerTin;
    }

    public void setSellerTin(String sellerTin) {
        this.sellerTin = sellerTin;
    }

    public String getPurchaserTin() {
        return purchaserTin;
    }

    public void setPurchaserTin(String purchaserTin) {
        this.purchaserTin = purchaserTin;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }
}
