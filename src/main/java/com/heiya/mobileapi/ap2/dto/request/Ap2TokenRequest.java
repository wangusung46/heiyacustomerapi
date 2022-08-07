package com.heiya.mobileapi.ap2.dto.request;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Khanza
 *
 */
@NoArgsConstructor
public class Ap2TokenRequest {

    @Getter
    @Setter
    private static String token;
    
    @Getter
    @Setter
    private static String storeId;
    
    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private static Date loginTime;
}
