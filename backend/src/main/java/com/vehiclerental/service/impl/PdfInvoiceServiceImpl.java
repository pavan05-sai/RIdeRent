package com.vehiclerental.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vehiclerental.entity.Booking;
import com.vehiclerental.entity.Payment;
import com.vehiclerental.entity.User;
import com.vehiclerental.entity.enums.RoleName;
import com.vehiclerental.exception.ForbiddenException;
import com.vehiclerental.exception.ResourceNotFoundException;
import com.vehiclerental.repository.BookingRepository;
import com.vehiclerental.repository.PaymentRepository;
import com.vehiclerental.repository.UserRepository;
import com.vehiclerental.service.PdfInvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfInvoiceServiceImpl implements PdfInvoiceService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    @Override
    @Transactional(readOnly = true)
    public ByteArrayInputStream generateInvoicePdf(Long bookingId, String userEmail) {
        Booking booking = getAuthorizedBooking(bookingId, userEmail);
        Payment payment = paymentRepository.findByBookingId(booking.getId()).orElse(null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Monochrome Fonts
            Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Font.BOLD, Color.BLACK);
            Font taglineFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC, Color.GRAY);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Font.BOLD, Color.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD, Color.BLACK);

            // Brand Header
            Paragraph brand = new Paragraph("RideRent", brandFont);
            brand.setAlignment(Element.ALIGN_LEFT);
            document.add(brand);

            Paragraph tagline = new Paragraph("Rent. Ride. Return.", taglineFont);
            tagline.setAlignment(Element.ALIGN_LEFT);
            tagline.setSpacingAfter(15);
            document.add(tagline);

            // Divider Line
            PdfPTable divider = new PdfPTable(1);
            divider.setWidthPercentage(100);
            PdfPCell divCell = new PdfPCell(new Phrase(""));
            divCell.setBorder(Rectangle.BOTTOM);
            divCell.setBorderColor(Color.LIGHT_GRAY);
            divCell.setPaddingBottom(5);
            divider.addCell(divCell);
            divider.setSpacingAfter(15);
            document.add(divider);

            // Invoice Title & Meta Table
            PdfPTable metaTable = new PdfPTable(2);
            metaTable.setWidthPercentage(100);

            PdfPCell leftMeta = new PdfPCell();
            leftMeta.setBorder(Rectangle.NO_BORDER);
            leftMeta.addElement(new Paragraph("COMMERCIAL TAX INVOICE", headerFont));
            leftMeta.addElement(new Paragraph("Invoice No: INV-" + booking.getBookingReference(), boldFont));
            leftMeta.addElement(new Paragraph("Booking Ref: " + booking.getBookingReference(), normalFont));
            leftMeta.addElement(new Paragraph("Issue Date: " + booking.getCreatedAt().format(DATE_FMT), normalFont));

            PdfPCell rightMeta = new PdfPCell();
            rightMeta.setBorder(Rectangle.NO_BORDER);
            rightMeta.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightMeta.addElement(new Paragraph("Billed To:", boldFont));
            rightMeta.addElement(new Paragraph(booking.getUser().getFullName(), boldFont));
            rightMeta.addElement(new Paragraph("Email: " + booking.getUser().getEmail(), normalFont));
            rightMeta.addElement(new Paragraph("Phone: " + booking.getUser().getPhone(), normalFont));

            metaTable.addCell(leftMeta);
            metaTable.addCell(rightMeta);
            metaTable.setSpacingAfter(20);
            document.add(metaTable);

            // Vehicle & Rental Details Table
            PdfPTable detailTable = new PdfPTable(2);
            detailTable.setWidthPercentage(100);
            detailTable.setSpacingAfter(20);

            addSectionHeaderCell(detailTable, "Vehicle Details", 1);
            addSectionHeaderCell(detailTable, "Rental Itinerary", 1);

            PdfPCell vCell = new PdfPCell();
            vCell.setPadding(8);
            vCell.setBorderColor(Color.LIGHT_GRAY);
            vCell.addElement(new Paragraph(booking.getVehicle().getBrand() + " " + booking.getVehicle().getModel() + " (" + booking.getVehicle().getYear() + ")", boldFont));
            vCell.addElement(new Paragraph("Registration: " + booking.getVehicle().getRegistrationNumber(), normalFont));
            vCell.addElement(new Paragraph("Class: " + booking.getVehicle().getVehicleType(), normalFont));
            vCell.addElement(new Paragraph("Fuel / Transmission: " + booking.getVehicle().getFuelType() + " / " + booking.getVehicle().getTransmission(), normalFont));

            PdfPCell rCell = new PdfPCell();
            rCell.setPadding(8);
            rCell.setBorderColor(Color.LIGHT_GRAY);
            rCell.addElement(new Paragraph("Pickup: " + booking.getPickupDate().format(DATE_FMT), normalFont));
            rCell.addElement(new Paragraph("Location: " + booking.getPickupLocation(), normalFont));
            rCell.addElement(new Paragraph("Return: " + booking.getReturnDate().format(DATE_FMT), normalFont));
            rCell.addElement(new Paragraph("Return Location: " + booking.getReturnLocation(), normalFont));

            detailTable.addCell(vCell);
            detailTable.addCell(rCell);
            document.add(detailTable);

            // Financial Breakdown Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 2f, 2f, 2f});
            table.setSpacingAfter(20);

            addTableHeader(table, "Description");
            addTableHeader(table, "Rate");
            addTableHeader(table, "Days / Units");
            addTableHeader(table, "Amount (INR)");

            addRow(table, "Vehicle Rental Base Charge", "₹" + booking.getVehicle().getPricePerDay() + "/day", "-", "₹" + booking.getBaseAmount());
            if (booking.getDiscountAmount() != null && booking.getDiscountAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                addRow(table, "Promotional Discount (" + (booking.getCoupon() != null ? booking.getCoupon().getCode() : "COUPON") + ")", "-", "-", "-₹" + booking.getDiscountAmount());
            }
            addRow(table, "Applicable Goods & Services Tax (GST 18%)", "18%", "-", "₹" + booking.getTaxAmount());
            addRow(table, "Refundable Security Deposit", "Standard", "-", "₹" + booking.getSecurityDeposit());

            // Total row
            PdfPCell totalLabelCell = new PdfPCell(new Phrase("Total Paid / Payable", boldFont));
            totalLabelCell.setColspan(3);
            totalLabelCell.setPadding(8);
            totalLabelCell.setBackgroundColor(new Color(245, 245, 245));
            table.addCell(totalLabelCell);

            PdfPCell totalValCell = new PdfPCell(new Phrase("₹" + booking.getTotalAmount(), boldFont));
            totalValCell.setPadding(8);
            totalValCell.setBackgroundColor(new Color(245, 245, 245));
            table.addCell(totalValCell);

            document.add(table);

            // Payment and Legal Note
            Paragraph payInfo = new Paragraph("Payment Status: " + (payment != null ? payment.getStatus() : "UNPAID") +
                    (payment != null ? " | Method: " + payment.getPaymentMethod() + " | Txn Ref: " + payment.getTransactionReference() : ""), boldFont);
            payInfo.setSpacingAfter(10);
            document.add(payInfo);

            Paragraph footer = new Paragraph("RideRent Automotive Mobility Services. Registered Office: Bengaluru, India. Computer generated invoice.", taglineFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating invoice PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayInputStream generateRentalAgreementPdf(Long bookingId, String userEmail) {
        Booking booking = getAuthorizedBooking(bookingId, userEmail);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font brandFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Font.BOLD, Color.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD, Color.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, Color.DARK_GRAY);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.BOLD, Color.BLACK);

            document.add(new Paragraph("RideRent Digital Rental Agreement", brandFont));
            document.add(new Paragraph("Rent. Ride. Return. | Agreement ID: AGR-" + booking.getBookingReference(), FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC, Color.GRAY)));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("1. PARTIES TO AGREEMENT", headerFont));
            document.add(new Paragraph("This Rental Agreement is entered into between RideRent Mobility Services ('Operator') and " + booking.getUser().getFullName() + " ('Hirer'), residing at email: " + booking.getUser().getEmail() + ", contact: " + booking.getUser().getPhone() + ".", normalFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("2. VEHICLE SPECIFICATION", headerFont));
            document.add(new Paragraph("Vehicle: " + booking.getVehicle().getBrand() + " " + booking.getVehicle().getModel() + " (" + booking.getVehicle().getYear() + ") | Reg: " + booking.getVehicle().getRegistrationNumber() + " | Fuel: " + booking.getVehicle().getFuelType() + " | Transmission: " + booking.getVehicle().getTransmission(), normalFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("3. RENTAL PERIOD & CHARGES", headerFont));
            document.add(new Paragraph("Pickup: " + booking.getPickupDate().format(DATE_FMT) + " at " + booking.getPickupLocation() + "\n" +
                    "Return: " + booking.getReturnDate().format(DATE_FMT) + " at " + booking.getReturnLocation() + "\n" +
                    "Total Charge: ₹" + booking.getTotalAmount() + " (Includes ₹" + booking.getSecurityDeposit() + " Security Deposit)", normalFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("4. TERMS & CONDITIONS", headerFont));
            document.add(new Paragraph("- Hirer acknowledges possession of a valid driving license for the respective vehicle class.\n" +
                    "- Sub-leasing, driving under the influence, or unauthorized off-road operation is strictly prohibited.\n" +
                    "- Vehicle must be returned with the same fuel level as dispatched.\n" +
                    "- The security deposit will be refunded within 24 hours of successful vehicle check-in.", normalFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Digitally acknowledged and confirmed under booking reference: " + booking.getBookingReference(), boldFont));

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating rental agreement PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private Booking getAuthorizedBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName() == RoleName.ROLE_ADMIN);
        if (!isAdmin && !booking.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Unauthorized to access this booking documentation");
        }

        return booking;
    }

    private void addSectionHeaderCell(PdfPTable table, String text, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE)));
        cell.setBackgroundColor(Color.BLACK);
        cell.setColspan(colspan);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addTableHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE)));
        cell.setBackgroundColor(Color.BLACK);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addRow(PdfPTable table, String col1, String col2, String col3, String col4) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);
        PdfPCell c1 = new PdfPCell(new Phrase(col1, font));
        PdfPCell c2 = new PdfPCell(new Phrase(col2, font));
        PdfPCell c3 = new PdfPCell(new Phrase(col3, font));
        PdfPCell c4 = new PdfPCell(new Phrase(col4, font));
        c1.setPadding(6);
        c2.setPadding(6);
        c3.setPadding(6);
        c4.setPadding(6);
        c1.setBorderColor(Color.LIGHT_GRAY);
        c2.setBorderColor(Color.LIGHT_GRAY);
        c3.setBorderColor(Color.LIGHT_GRAY);
        c4.setBorderColor(Color.LIGHT_GRAY);
        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);
        table.addCell(c4);
    }

    public PdfInvoiceServiceImpl(BookingRepository bookingRepository, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }
}
