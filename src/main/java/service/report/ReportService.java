package service.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import repository.sales.SaleRepository;
import view.model.ReportDTO;

import java.io.FileOutputStream;
import java.util.List;

public class ReportService {

    private final SaleRepository saleRepository;

    public ReportService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public void generateReport() {
        List<ReportDTO> data = saleRepository.getReportDataForLastMonth();

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("MonthlyReport.pdf"));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
            Paragraph title = new Paragraph("Employee Sales Report - Last Month", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(3);
            addTableHeader(table);
            addRows(table, data);

            document.add(table);
            document.close();

            System.out.println("Report generated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table) {
        table.addCell(createHeaderCell("Employee"));
        table.addCell(createHeaderCell("Total Revenue (RON)"));
        table.addCell(createHeaderCell("Books Sold"));
    }

    private PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(text));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private void addRows(PdfPTable table, List<ReportDTO> data) {
        for (ReportDTO report : data) {
            table.addCell(report.getEmployee());
            table.addCell(String.valueOf(report.getTotalRevenue()));
            table.addCell(String.valueOf(report.getTotalSales()));
        }
    }
}