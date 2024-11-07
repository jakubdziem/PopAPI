document.addEventListener('DOMContentLoaded', function () {
  async function fetchAndRenderChart() {
    const response = await fetch('/stats_for_chart');
    const statsData = await response.json();

    const filteredData = statsData.filter(stat => stat.timePlayed !== '00:00:00' || stat.totalScoredPoints > 0);

    const timePlayedInMinutes = filteredData.map(stat => {
      const [hours, minutes] = stat.timePlayed.split(':').map(Number);
      return hours * 60 + minutes;
    });
    const totalScoredPoints = filteredData.map(stat => stat.totalScoredPoints);

    if (timePlayedInMinutes.length === 0 || totalScoredPoints.length === 0) {
      console.error('No valid data available to render the chart');
      return;
    }


    const ctx = document.getElementById('myChart').getContext('2d');
    const myChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: timePlayedInMinutes,
        datasets: [{
          label: 'Total Scored Points',
          data: totalScoredPoints,
          lineTension: 0,
          backgroundColor: 'transparent',
          borderColor: '#007bff',
          borderWidth: 4,
          pointBackgroundColor: '#007bff'
        }]
      },
      options: {
        scales: {
          x: {
            type: 'linear',
            position: 'bottom',
            title: {
              display: true,
              text: 'Time Played (Minutes)'
            },
            ticks: {
              callback: (value) => `${value} min`
            }
          },
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: 'Total Scored Points'
            },
            ticks: {
              stepSize: 500
            }
          }
        },
        plugins: {
          legend: {
            display: true
          },
          title: {
            display: true,
            text: 'Time Played vs. Total Scored Points'
          }
        }
      }
    });
  }

  fetchAndRenderChart();


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
