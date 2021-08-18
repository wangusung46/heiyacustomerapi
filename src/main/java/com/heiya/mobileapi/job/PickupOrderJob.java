/**
 * 
 */
package com.heiya.mobileapi.job;

import com.heiya.mobileapi.firebase.service.PushNotificationService;
import com.heiya.mobileapi.payment.controller.PaymentController;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.heiya.mobileapi.payment.service.PaymentService;

/**
 * @author Dian Krisnanjaya
 *
 */
public class PickupOrderJob implements Job {
	
	@Autowired
	private PaymentService paymentService;
        
        @Autowired
	private PaymentController paymentController;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			//Job for Pickup expiry
			paymentService.expirePickupOrder();
			
			//Job for Payment status
			paymentService.checkAndUpdateTransactionStatus();
                        
                        //Notification expier
//                        paymentService.checkTransactionStatusNotification();
                        
                        paymentController.doCheckTransactionStatusNotification();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
