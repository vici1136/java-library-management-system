package view.model;

public class ReportDTO {
    private String employee;
    private Double totalRevenue;
    private Integer totalSales;

    public ReportDTO(String employee, Double totalRevenue, Integer totalSales) {
        this.employee = employee;
        this.totalRevenue = totalRevenue;
        this.totalSales = totalSales;
    }

    public String getEmployee() { return employee; }
    public Double getTotalRevenue() { return totalRevenue; }
    public Integer getTotalSales() { return totalSales; }

    @Override
    public String toString() {
        return "Employee: " + employee + " | Total: " + totalRevenue + " | Books: " + totalSales;
    }
}