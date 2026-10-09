package com.vehiclerental.service;

import java.io.ByteArrayInputStream;

public interface PdfInvoiceService {
    ByteArrayInputStream generateInvoicePdf(Long bookingId, String userEmail);
    ByteArrayInputStream generateRentalAgreementPdf(Long bookingId, String userEmail);
}
