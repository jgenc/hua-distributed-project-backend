package gr.hua.dit.distributed.project_46.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CompleteDeclarationRequest {

    @NotBlank(message="Please enter the Contract Details")
    @Size(min = 12, max = 25, message = "Contract Details should be between 12 and 25 characters")
    private String contractDetails;

    @NotBlank(message="Please enter the Payment Method")
    @Size(min = 3, max = 30, message = "Payment Method should be between 12 and 25 characters")
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
