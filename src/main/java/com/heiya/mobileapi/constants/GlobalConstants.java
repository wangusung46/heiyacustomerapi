/**
 *
 */
package com.heiya.mobileapi.constants;

/**
 * @author Dian Krisnanjaya
 *
 */
public class GlobalConstants {

    //Common
    public static final String CHANNEL_TYPE_MIDTRANS = "midtrans";
    public static final String CHANNEL_TYPE_XENDIT = "xendit";
    public static final String CHANNEL_TYPE_PARTIAL = "partial";
    public static final String EWALLET_BASE_CURRENCY = "IDR";
    public static final String TRX_STATUS_SETTLEMENT = "settlement";
    public static final String TRX_STATUS_PENDING = "pending";
    public static final String TRX_STATUS_EXPIRE = "expired";
    public static final String TRX_STATUS_UNPAID = "unpaid";
    public static final String TRX_STATUS_UNDELIVERED = "undelivered";
    public static final String TRX_STATUS_DELIVERING = "delivering";
    public static final String TRX_STATUS_COMPLETED = "complete";
    public static final String TRX_STATUS_PICKUPEXPIRED = "pickup_expired";

    //DB Global Configs
    public static final String PAY_GW_GLOBAL_ACTIVE = "pay_gateway_global_active";
    public static final String TRX_TIMEOUT_DURATION = "trx_timeout_duration";

    public static final String PAY_GW_URL1 = "pay_gateway_url1";
    public static final String PAY_GW_KEY1 = "pay_gateway_key1";
    public static final String PAY_GW_PROVIDER1 = "pay_gateway_provider1";
    public static final String PAY_GW_PROVIDER2 = "pay_gateway_provider2";
    public static final String PAY_GW_ENABLE_CALLBACK1 = "pay_gateway_enable_callback1";
    public static final String PAY_GW_CALLBACK_URL1 = "pay_gateway_callback_url1";
    public static final String CLIENT_URL1 = "client_url1";
    public static final String CLIENT_URL2 = "client_url2";
    public static final String CLIENT_IP_WHITELIST = "client_ip_whitelist";

    public static final String PAY_GW_XENDIT_URL = "pay_gateway_xendit_url";
    public static final String PAY_GW_XENDIT_URL_NEW = "pay_gateway_xendit_url_new";
    public static final String PAY_GW_XENDIT_KEY = "pay_gateway_xendit_key";
    public static final String PAY_GW_XENDIT_QRTYPE = "pay_gateway_xendit_qrtype";
    public static final String PAY_GW_XENDIT_CALLBACKURL = "pay_gateway_xendit_callbackurl";
    public static final String PAY_GW_XENDIT_PROVIDER = "pay_gateway_xendit_provider";

    public static final String PAY_GW_XENDIT_QRIS_IMAGEURL = "pay_gateway_xendit_qris_imageurl";
    public static final String PAY_GW_XENDIT_QRIS_DIR = "pay_gateway_qris_directory";

    public static final String HW_QRIS_TYPE = "hw_qris_type";
    public static final String HW_QRIS_TYPE_1 = "1";
    public static final String HW_QRIS_TYPE_2 = "2";
    public static final String QR_IMAGE_WIDTH = "qr_image_width";
    public static final String QR_IMAGE_HEIGHT = "qr_image_height";

    public static final String OTP_DEFAULT_GW = "otp_default_gateway";

    //Sms Provider
    public static final String SMS_GW_MITRACOMM_URL = "sms_gateway_mitracomm_url";
    public static final String SMS_GW_MITRACOMM_USERID = "sms_gateway_mitracomm_userid";
    public static final String SMS_GW_MITRACOMM_PASSWORD = "sms_gateway_mitracomm_password";
    public static final String SMS_GW_MITRACOMM_MASKING = "sms_gateway_mitracomm_masking";
    public static final String SMS_GW_MITRACOMM_APPCODE = "sms_gateway_mitracomm_appcode";
    public static final String SMS_GW_MITRACOMM_TYPE = "sms_gateway_mitracomm_type";

    //Mobile
    public static final String MOBILE = "mobile";
    public static final String OPERATE_CODE = "heiya_operate_code";
    public static final String ORDER_SOURCE = "userDefined";
    public static final String CHANNEL_TYPE = "channel_type";
    public static final String EWALLET_TYPE_GOPAY = "GOPAY";
    public static final String EWALLET_TYPE_OVO = "OVO";
    public static final String EWALLET_TYPE_DANA = "DANA";
    public static final String EWALLET_TYPE_LINKAJA = "LINKAJA";
    public static final String EWALLET_TYPE_SHOPEEPAY = "SHOPEEPAY";
    public static final String EWALLET_TYPE_OVO_NEW = "ID_OVO";
    public static final String EWALLET_TYPE_DANA_NEW = "ID_DANA";
    public static final String EWALLET_TYPE_LINKAJA_NEW = "ID_LINKAJA";
    public static final String EWALLET_TYPE_SHOPEEPAY_NEW = "ID_SHOPEEPAY";
    public static final String PRODUCT_IMG_URL = "product_images_path";
    public static final String PAY_GW_DANA_CALLBACKURL = "pay_gateway_xendit_dana_callbackurl";
    public static final String PAY_GW_LINKAJA_CALLBACKURL = "pay_gateway_xendit_linkaja_callbackurl";
    public static final String PAYMENT_STATUS_TRIGGER_JOB = "job";
    public static final String PAYMENT_STATUS_TRIGGER_APP = "app";
    public static final String VERSION_MOBILE_ANDROID = "mobile_android";
    public static final String VERSION_MOBILE_IOS = "mobile_ios";

    //Notification
    public static final String MEDIA_NOTIFICATION = "media_expire_notification";
    public static final String MEDIA_NOTIFICATION_FIREBASE = "firebase";
    public static final String MEDIA_NOTIFICATION_OTP = "otp";
    public static final String PAYMENT_NOTIFICATION = "payment_notification";
    public static final String EXPIRE_NOTIFICATION = "expire_notification";
    public static final String NOTIFICATION_FIREBASE_CHECK_PAYMENT = "CHECK_PAYMENT";
    public static final String NOTIFICATION_FIREBASE_CHECK_EXPIRE = "CHECK_PICKUP";
    public static final String NOTIFICATION_FIREBASE_EXPIRE_PAYMENT = "EXPIRE_PAYMENT";
    public static final String NOTIFICATION_FIREBASE_EXPIRE_PICKUP = "EXPIRE_PICKUP";
    public static final String NOTIFICATION_FIREBASE_SECCESS_PAYMENT = "SECCESS_PAYMENT";
    public static final String NOTIFICATION_FIREBASE_SECCESS_PICKUP = "SECCESS_PICKUP";
    public static final String SOUND_NOTIFICATION = "default";
    public static final String COLOR_NOTIFICATION = "#FFFF00";

    //AP2
    public static final String AP2_ACTIVE = "ap2_active";
    public static final String AP2_URL = "ap2_url";
    public static final String AP2_USERNAME = "ap2_username";
    public static final String AP2_PASSWORD = "ap2_password";
    public static final String AP2_GET_TRANSACTION = "ap2_transaction";
    
    //Email
    public static final String EMAIL_TO = "email_to";
    public static final String SUBJECT = "subject";
}
