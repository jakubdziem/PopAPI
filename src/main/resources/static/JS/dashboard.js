document.addEventListener('DOMContentLoaded', function () {
  async function fetchAndRenderChart() {
    const modeSelector = document.getElementById("modeSelect");
    const selectedMode = modeSelector.value;
    try {
      const response = await fetch(`/stats_for_chart/${selectedMode}`);
      if (!response.ok) {
        throw new Error(`Error fetching stats: ${response.status}`);
      }
      const statsData = await response.json();
      let data;
      const labels = statsData.map(stat => stat.day); // X-axis: Days
      const attributeSelect = document.getElementById("attributeSelect");

      function timeToSeconds(timeStr) {
        const [hours, minutes, seconds] = timeStr.split(":").map(Number);
        return (hours * 3600) + (minutes * 60) + seconds;
      }

      switch (attributeSelect.value) {
        case "totalGamePlayed":
          data = statsData.map(stat => stat.totalGamePlayed);
          break;
        case "avgScore":
          data = statsData.map(stat => stat.avgScore);
          break;
        case "timePlayed":
          data = statsData.map(stat => timeToSeconds(stat.timePlayed));
          break;
        case "totalScoredPoints":
          data = statsData.map(stat => stat.totalScoredPoints);
          break;
        case "numberOfWonGames":
          data = statsData.map(stat => stat.numberOfWonGames);
          break;
        default:
          data = statsData.map(stat => stat.totalGamePlayed);
          break;
      }

      if (labels.length === 0 || data.length === 0) {
        console.error('No valid data available to render the chart');
        return;
      }

      const ctx = document.getElementById('myChart').getContext('2d');
      new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: 'Games Played',
            data: data,
            lineTension: 0.3,
            backgroundColor: 'rgba(0, 123, 255, 0.1)',
            borderColor: '#007bff',
            borderWidth: 2,
            pointBackgroundColor: '#007bff'
          }]
        },
        options: {
          responsive: true,
          scales: {
            x: {
              type: 'time',
              time: {
                unit: 'day',
                tooltipFormat: 'MMM D',
                displayFormats: {
                  day: 'MMM D'
                }
              },
              title: {
                display: true,
                text: 'Day'
              }
            },
            y: {
              min: 0,
              title: {
                display: true,
                text: 'Total Games Played'
              }
            }
          },
          plugins: {
            legend: {
              display: true
            },
            title: {
              display: true,
              text: `Games Played Over Time for Mode: ${selectedMode}`
            }
          }
        }
      });
    } catch (error) {
      console.error("Error rendering chart:", error);
    }
  }

  const modeSelector = document.getElementById("modeSelect");
  modeSelector.addEventListener("change", fetchAndRenderChart);

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
