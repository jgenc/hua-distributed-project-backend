package gr.hua.dit.distributed.project_46.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "declarations")
public class Declaration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private EStatus status;

    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "notarytin", referencedColumnName = "tin")
    @JsonManagedReference
    private Person notary;

    @Column(name = "propertynumber", length = 15)
    @NotBlank
    private String propertyNumber;

    @Column(name = "propertycategory", length = 15)
    @NotBlank
    private String propertyCategory;

    @Column(name = "propertydescription", length = 100)
    @NotBlank
    private String propertyDescription;

    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sellertin", referencedColumnName = "tin")
    @JsonManagedReference
    private Person seller;

    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "purchaserTin", referencedColumnName = "tin")
    @JsonManagedReference
    private Person purchaser;

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

    public Declaration(String propertyNumber, String propertyCategory, String propertyDescription, Float tax) {
        this.status=EStatus.pending;
        this.propertyNumber = propertyNumber;
        this.propertyCategory = propertyCategory;
        this.propertyDescription = propertyDescription;
        this.tax = tax;
        this.sellerAcceptance = false;
        this.purchaserAcceptance = false;
        this.contractDetails = "";
        this.paymentMethod = "";
        this.notary=null;
        this.seller=null;
        this.purchaser=null;
    }

    public Declaration(EStatus status, String propertyNumber, String propertyCategory, String propertyDescription, Float tax, Boolean sellerAcceptance, Boolean purchaserAcceptance, String contractDetails, String paymentMethod) {
        this.status = status;
        this.propertyNumber = propertyNumber;
        this.propertyCategory = propertyCategory;
        this.propertyDescription = propertyDescription;
        this.tax = tax;
        this.sellerAcceptance = sellerAcceptance;
        this.purchaserAcceptance = purchaserAcceptance;
        this.contractDetails = contractDetails;
        this.paymentMethod = paymentMethod;
        this.notary=null;
        this.seller=null;
        this.purchaser=null;
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

    public Person getNotary() {
        return notary;
    }

    public void setNotary(Person notary) {
        this.notary = notary;
    }

    public Person getSeller() {
        return seller;
    }

    public void setSeller(Person seller) {
        this.seller = seller;
    }

    public Person getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(Person purchaser) {
        this.purchaser = purchaser;
    }

 }
