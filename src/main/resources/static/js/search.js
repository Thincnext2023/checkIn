function checkOut(id) {
    fetch(`/user/checkOut/${id}`, {
        method: 'PUT'
    })
        .then(response => response.text())
        .then(message => alert(message))
        .catch(error => console.error('Error:', error));
}

function clearField() {
    document.getElementById("searchInput").value = ""
    document.getElementById("searchFrom").value = ""
    document.getElementById("searchTo").value = ""

}
// 🔍 Search Function
function searchTable() {
    const input = document.getElementById("searchInput").value.toLowerCase();
    const rows = document.querySelectorAll("#checkInTable tbody tr");

    rows.forEach(row => {
        const cells = row.querySelectorAll("td");
        let match = false;

        cells.forEach(cell => {
            if (cell.textContent.toLowerCase().includes(input)) {
                match = true;
            }
        });

        row.style.display = match ? "" : "none"; // Show/Hide rows
    });
}

// 📅 Search By Date
function searchByDate() {
    const fromDate = new Date(document.getElementById("searchFrom").value);
    const toDate = new Date(document.getElementById("searchTo").value);
    const rows = document.querySelectorAll("#checkInTable tbody tr");

    rows.forEach(row => {
        const createdAt = new Date(row.cells[5].textContent); // Assuming "Created At" is in the 6th column
        const inRange = (!isNaN(fromDate) ? createdAt >= fromDate : true) &&
            (!isNaN(toDate) ? createdAt <= toDate : true);
        row.style.display = inRange ? "" : "none";
    });
}
history.pushState(null, '', location.href);
window.onpopstate = function () {
    history.pushState(null, '', location.href);
};