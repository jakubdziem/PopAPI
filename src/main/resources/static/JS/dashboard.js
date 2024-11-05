document.addEventListener('DOMContentLoaded', function () {
  // Replace feather icons
  feather.replace({ 'aria-hidden': 'true' });

  // Chart setup
  const ctx = document.getElementById('myChart');
  const myChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
      datasets: [{
        data: [15339, 21345, 18483, 24003, 23489, 24092, 12034],
        lineTension: 0,
        backgroundColor: 'transparent',
        borderColor: '#007bff',
        borderWidth: 4,
        pointBackgroundColor: '#007bff'
      }]
    },
    options: {
      scales: {
        yAxes: [{
          ticks: {
            beginAtZero: false
          }
        }]
      },
      legend: {
        display: false
      }
    }
  });

  // Sorting setup
  let sortOrder = {}; // Track sorting order for each column

  function customSortTable(header, columnIndex) {
    const table = header.closest('table');
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.rows);
    const isAsc = header.classList.contains('sort-asc');

    // Remove sorting classes from other headers
    table.querySelectorAll('th').forEach(th => {
      th.classList.remove('sort-asc', 'sort-desc');
    });

    // Numeric columns based on index (0-based index)
    const numericColumns = [2, 3, 5]; // Update this array if your numeric columns change

    // Sort rows based on the column index
    rows.sort((a, b) => {
      const cellA = a.cells[columnIndex].textContent.trim();
      const cellB = b.cells[columnIndex].textContent.trim();

      // Check if the column is numeric
      if (numericColumns.includes(columnIndex)) {
        // Convert to numbers for numeric columns
        return isAsc ? Number(cellA) - Number(cellB) : Number(cellB) - Number(cellA);
      } else {
        // Default string comparison
        return isAsc ? cellA.localeCompare(cellB) : cellB.localeCompare(cellA);
      }
    });

    // Append sorted rows back to the tbody
    rows.forEach(row => tbody.appendChild(row));

    // Toggle sorting classes for the clicked header
    header.classList.toggle('sort-asc', !isAsc);
    header.classList.toggle('sort-desc', isAsc);
  }

  // Attach click event listeners to each column header
  document.querySelectorAll("th[data-sort]").forEach(header => {
    header.addEventListener("click", function () {
      const columnIndex = parseInt(header.getAttribute("data-sort"), 10);
      customSortTable(header, columnIndex);
    });
  });
});
