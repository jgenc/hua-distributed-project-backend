package gr.hua.dit.distributed.project_46.entity;
import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "declarations")
public class Declaration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private EStatus status;

    @Column(name = "notarytin", length = 9)
    @NotBlank
    private String notaryTin;

    @Column(name = "propertynumber", length = 15)
    @NotBlank
    private String propertyNumber;

    @Column(name = "propertycategory", length = 15)
    @NotBlank
    private String propertyCategory;

    @Column(name = "propertydescription", length = 100)
    @NotBlank
    private String propertyDescription;

    @Column(name = "sellertin", length = 9)
    @NotBlank
    private String sellerTin;

    @Column(name = "purchasertin", length = 9)
    @NotBlank
    private String purchaserTin;

    @Column(name = "tax")
    private Float tax;

    @Column(name = "selleracceptance")
    private Boolean sellerAcceptance;

    @Column(name = "purchaseracceptance")
    private Boolean purchaserAcceptance;

    @Column(name = "contractdetails", length = 25) // contractNumber/contractDate eg. 2345/20-10-2022
    private String contractDetails;

    @Column(name = "paymentmethod", length = 30)
    private String paymentMethod;


    public Declaration() {
    }

    public Declaration(String notaryTin, String propertyNumber, String propertyCategory, String propertyDescription, String sellerTin, String purchaserTin, Float tax) {
        this.status=EStatus.pending;
        this.notaryTin = notaryTin;
        this.propertyNumber = propertyNumber;
        this.propertyCategory = propertyCategory;
        this.propertyDescription = propertyDescription;
        this.sellerTin = sellerTin;
        this.purchaserTin = purchaserTin;
        this.tax = tax;
        this.sellerAcceptance = false;
        this.purchaserAcceptance = false;
        this.contractDetails = "";
        this.paymentMethod = "";
    }

    public Declaration(EStatus status, String notaryTin, String propertyNumber, String propertyCategory, String propertyDescription, String sellerTin, String purchaserTin, Float tax, Boolean sellerAcceptance, Boolean purchaserAcceptance, String contractDetails, String paymentMethod) {
        this.status = status;
        this.notaryTin = notaryTin;
        this.propertyNumber = propertyNumber;
        this.propertyCategory = propertyCategory;
        this.propertyDescription = propertyDescription;
        this.sellerTin = sellerTin;
        this.purchaserTin = purchaserTin;
        this.tax = tax;
        this.sellerAcceptance = sellerAcceptance;
        this.purchaserAcceptance = purchaserAcceptance;
        this.contractDetails = contractDetails;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

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

    public Boolean getSellerAcceptance() {
        return sellerAcceptance;
    }

    public void setSellerAcceptance(Boolean sellerAcceptance) {
        this.sellerAcceptance = sellerAcceptance;
    }

    public Boolean getPurchaserAcceptance() {
        return purchaserAcceptance;
    }

    public void setPurchaserAcceptance(Boolean purchaserAcceptance) {
        this.purchaserAcceptance = purchaserAcceptance;
    }

    public String getContractDetails() {
        return contractDetails;
    }

    public void setContractDetails(String contractDetails) {
        this.contractDetails = contractDetails;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
