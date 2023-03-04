package com.heiya.mobileapi.util;

public abstract class AbstractCommon {

    protected ConcurrentDateFormatAccess formatDate = new ConcurrentDateFormatAccess("dd-MM-yyyy");
    protected ConcurrentDateFormatAccess formatDateDb = new ConcurrentDateFormatAccess("MM-dd-yyyy");
    protected ConcurrentDateFormatAccess formatDateId = new ConcurrentDateFormatAccess("dd/MM/yyyy");
    protected ConcurrentDateFormatAccess formatDateddMMMMyyyy = new ConcurrentDateFormatAccess("dd MMMM yyyy");
    protected ConcurrentDateFormatAccess formatDateMMMyyyy = new ConcurrentDateFormatAccess("MMM yyyy");
    protected ConcurrentDateFormatAccess formatDateMMMMyyyy = new ConcurrentDateFormatAccess("MMMM yyyy");
    protected ConcurrentDateFormatAccess formatDateSql = new ConcurrentDateFormatAccess("yyyy-MM-dd");
    protected ConcurrentDateFormatAccess formatTimestamp = new ConcurrentDateFormatAccess("yyyyMMddHHmmssSSS");
    protected ConcurrentDateFormatAccess formatDateMon1 = new ConcurrentDateFormatAccess("dd-MMM-yy");
    protected ConcurrentDateFormatAccess formatDateMon2 = new ConcurrentDateFormatAccess("dd MMM yyyy");
    protected ConcurrentDateFormatAccess formatDateMon3 = new ConcurrentDateFormatAccess("dd-MMM-yyyy");
}
