package com.sap.cloud.sdk.aws.tutorials;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.purchaserequisition.PurReqnAcctAssgmt;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.purchaserequisition.PurchaseRequisitionHeader;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.purchaserequisition.PurchaseRequisitionItem;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultPurchaseRequisitionService;

/**
 * Servlet implementation class PurcahseRequisitionServlet
 */
@WebServlet("/purcahserequisitions")
public class PurcahseRequisitionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PurcahseRequisitionServlet.class);

    private static final String apikey = "G1Ch48sgQBOuPpOjew3HjOc3ykloeHO3";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PurcahseRequisitionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {

			List<PurchaseRequisitionHeader> list = new DefaultPurchaseRequisitionService()
			.getAllPurchaseRequisitionHeader()
			.withCustomHttpHeaders(requestHeaders())
			.onRequestAndImplicitRequests()
			.select(PurchaseRequisitionHeader.PURCHASE_REQUISITION,
					PurchaseRequisitionHeader.PURCHASE_REQUISITION_TYPE,
					PurchaseRequisitionHeader.TO_PURCHASE_REQN_ITEM
					.select(PurchaseRequisitionItem.PURCHASE_REQUISITION_ITEM,
							PurchaseRequisitionItem.BASE_UNIT,
							PurchaseRequisitionItem.ITEM_NET_AMOUNT,
							PurchaseRequisitionItem.MATERIAL,
							PurchaseRequisitionItem.ORDERED_QUANTITY,
							PurchaseRequisitionItem.PROCESSING_STATUS,
							PurchaseRequisitionItem.PUR_REQN_PRICE_QUANTITY))
			.execute(new ErpConfigContext("MyErpSystem"));
			response.setContentType("application/json");
			response.getWriter().write(new Gson().toJson(list));
		} catch (ODataException e) {
			logger.error("error getting requistion", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            e.printStackTrace(response.getWriter());
			e.printStackTrace();
		}
	}

	private Map<String, String> requestHeaders(){
    	Map<String, String> requestHeaders = new HashMap<>();
    	requestHeaders.put("Content-Type","application/json");
    	requestHeaders.put("APIKey",apikey);
    	return requestHeaders;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		new DefaultPurchaseRequisitionService()
//		.createPurchaseRequisitionHeader(purchaseRequisitionHeader)
		PurchaseRequisitionHeader purchaseRequisitionHeader = PurchaseRequisitionHeader.builder()
		.purReqnDescription("SathyaRequisition Created from odata api")
		.purchaseRequisitionType("NB")
		.purchaseReqnItem(PurchaseRequisitionItem.builder()
				.baseUnit("EA")
				.purchaseRequisitionItemText("Sathya-ReqItem")
				.orderedQuantity(new BigDecimal(100))
				.plant("1010")
				.companyCode("1010")
				.purReqnPriceQuantity(new BigDecimal(100))
				.requestedQuantity(new BigDecimal(100))
				.purchaseRequisitionPrice(new BigDecimal(100))
				.purReqnPriceQuantity(new BigDecimal(1))
				.purReqnItemCurrency("USD")
				.materialGroup("L001")
				.accountAssignmentCategory("K")
				.purchasingGroup("001")
				.purchasingOrganization("1010")
				.purchaseReqnAcctAssgmt(PurReqnAcctAssgmt.builder()
						.costElement("51100000")
						.gLAccount("51100000")
						.costCenter("10101101")
						.unloadingPointName("Gene Halas")
						.purchaseReqnAcctAssgmtNumber("1")
						.multipleAcctAssgmtDistrPercent(new BigDecimal(0)).build())
				.build())
		.build();
		
		try {
			PurchaseRequisitionHeader header = new DefaultPurchaseRequisitionService()
			.createPurchaseRequisitionHeader(purchaseRequisitionHeader)
			.withCustomHttpHeaders(requestHeaders())
			.onRequestAndImplicitRequests()
			.execute(new ErpConfigContext("MyErpSystem"));
			response.setContentType("application/json");
			response.getWriter().write(new Gson().toJson(header));
		} catch (ODataException e) {
			// TODO Auto-generated catch block
			logger.error("error creating requistion", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            e.printStackTrace(response.getWriter());
			e.printStackTrace();
		}
	}

}
