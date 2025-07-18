package com.heiya.mobileapi.ap2.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Khanza
 *
 * "store": [ { "store_id": "{Store Id[1]}", "airport_code": "{Airport}",
 * "store_name": "{Store Name}", "store_reference": null }, { "store_id":
 * "{Store Id[2]}", "airport_code": "{Airport}", "store_name": "{Store Name}",
 * "store_reference": null } ]
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ap2SuccessDataResponse {

    @JsonProperty("invoice_number")
    private String invoiceNo;

    @JsonProperty("transaction_date")
    private String transDate;

    @JsonProperty("transaction_time")
    private String transTime;

    @JsonProperty("sequence")
    private String sequenceUnique;

    @JsonProperty("item_code")
    private String itemCode;

    @JsonProperty("item_name")
    private String itemName;

    @JsonProperty("item_price_unit")
    private String itemPricePerUnit;

    @JsonProperty("item_price_amount")
    private String itemPriceAmount;

    @JsonProperty("item_total_price_amount")
    private String itemTotalPriceAmount;

    @JsonProperty("item_vat")
    private String itemVat;

    @JsonProperty("transaction_amount")
    private String transactionAmount;

    @JsonProperty("item_barcode")
    private String itemBarcode;

    @JsonProperty("item_category_name")
    private String itemCatName;

    @JsonProperty("item_category_code")
    private String itemCatCode;

    @JsonProperty("item_quantity")
    private String itemQty;

    @JsonProperty("item_unit")
    private String itemUnit;

    @JsonProperty("item_discount")
    private String itemDiscount;

    @JsonProperty("item_tax")
    private String itemTax;

    @JsonProperty("item_total_discount")
    private String itemTotalDiscount;

    @JsonProperty("item_total_vat")
    private String itemTotalVat;

    @JsonProperty("item_total_tax")
    private String itemTotalTax;

    @JsonProperty("item_total_service_charge")
    private String itemTotalServiceCharge;

    @JsonProperty("invoice_tax")
    private String invoiceTax;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("rate")
    private String rate;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("payment_by")
    private String paymentBy;

    @JsonProperty("username")
    private String username;

    @JsonProperty("buyer_barcode")
    private String buyerBarcode;

    @JsonProperty("buyer_name")
    private String buyerName;

    @JsonProperty("buyer_flight_no")
    private String buyerFlightNo;

    @JsonProperty("buyer_destination")
    private String buyerDestination;

    @JsonProperty("buyer_nationality")
    private String buyerNationality;

    @JsonProperty("remark")
    private String remark;

    @JsonProperty("tax_id")
    private String taxId;

    @JsonProperty("payment_name")
    private String paymentName;

    @JsonProperty("payment_time")
    private String paymentTime;

    @JsonProperty("distance")
    private String distance;

    @JsonProperty("journey_time")
    private String journeyTime;
}
