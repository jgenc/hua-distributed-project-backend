package gr.hua.dit.distributed.project_46.payload.request;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddDeclarationRequest {

    @NotBlank(message="Please enter the Property Number")
    @Size(min = 1, max = 15, message = "Property Number should not be greater than 15 characters")
    private String propertyNumber;

    @NotBlank(message="Please enter the Property Category")
    @Size(min = 1, max = 15, message = "Property Category should not be greater than 15 characters")
    private String propertyCategory;

    @NotBlank(message="Please enter the Property Description")
    @Size(min = 3, max = 100, message = "Property Description should not be greater than 100 characters")
    private String propertyDescription;

    @NotBlank(message="Please enter the Seller's Taxpayer Identification Number")
    @Size(min = 9, max = 9, message = "Seller's Taxpayer Identification Number should be 9 characters")
    private String sellerTin;

    @NotBlank(message="Please enter the Purchaser's Taxpayer Identification Number")
    @Size(min = 9, max = 9, message = "Purchaser's Taxpayer Identification Number should be 9 characters")
    private String purchaserTin;

    private Float tax;


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
