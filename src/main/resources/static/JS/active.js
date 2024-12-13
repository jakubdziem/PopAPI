document.addEventListener('DOMContentLoaded', function () {
let sortOrder = {};
function customSortTable(header, columnIndex) {
    const table = header.closest('table');
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.rows);
    const isAsc = header.classList.contains('sort-asc');
    table.querySelectorAll('th').forEach(th => {
        th.classList.remove('sort-asc', 'sort-desc');
    });
    const numericColumns = [2, 3, 5];
    rows.sort((a, b) => {
        const cellA = a.cells[columnIndex].textContent.trim();
        const cellB = b.cells[columnIndex].textContent.trim();
        if (numericColumns.includes(columnIndex)) {
            return isAsc ? Number(cellA) - Number(cellB) : Number(cellB) - Number(cellA);
        } else {
            return isAsc ? cellA.localeCompare(cellB) : cellB.localeCompare(cellA);
        }
    });
    rows.forEach(row => tbody.appendChild(row));
    header.classList.toggle('sort-asc', !isAsc);
    header.classList.toggle('sort-desc', isAsc);
}
document.querySelectorAll("th[data-sort]").forEach(header => {
    header.addEventListener("click", function () {
        const columnIndex = parseInt(header.getAttribute("data-sort"), 10);
        customSortTable(header, columnIndex);
    });
});
});