function downloadPDF() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    doc.text("Check-In Details", 14, 16);
    doc.autoTable({ html: '#checkInTable', startY: 20 });
    doc.save("CheckInDetails.pdf");
}
// 📊 Download as Excel
function downloadExcel() {
    const table = document.getElementById("checkInTable");
    const wb = XLSX.utils.table_to_book(table, { sheet: "CheckInDetails" });
    XLSX.writeFile(wb, "CheckInDetails.xlsx");
}