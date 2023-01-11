package gr.hua.dit.distributed.project_46.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CompleteDeclarationRequest {

    @NotBlank
    @Size(min = 12, max = 25)
    private String contractDetails;

    @NotBlank
    @Size(min = 3, max = 30)
    private String paymentMethod;

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
