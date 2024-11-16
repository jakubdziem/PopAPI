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

      function secondsToTime(seconds) {
        if (typeof seconds !== 'number' || isNaN(seconds)) {
          console.error("Invalid seconds value:", seconds);  // Check if value is invalid
          return 'Invalid Time';
        }
        const hrs = Math.floor(seconds / 3600);
        const mins = Math.floor((seconds % 3600) / 60);
        const secs = seconds % 60;
        return `${hrs}:${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
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

      console.log('Data:', data);  // Log the data to check its structure
      const ctx = document.getElementById('myChart').getContext('2d');
      new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: attributeSelect.value,
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
              title: {
                display: true,
                text: 'Day'
              }
            },
            y: {
              min: 0,
              ticks: {
                callback: function (value) {
                  return attributeSelect.value === "timePlayed" ? secondsToTime(value) : value;
                }
              },
              title: {
                display: attributeSelect.value === "timePlayed",
                text: 'Time Played (HH:MM:SS)'
              }
            }
          },
          plugins: {
            legend: {
              display: true
            },
            title: {
              display: false,
              text: `Games Played Over Time for Mode: ${selectedMode}`
            },
            tooltip: {
              callbacks: {
                label: function (tooltipItem) {
                  const rawValue = tooltipItem.raw;
                  return attributeSelect.value === "timePlayed" ? secondsToTime(rawValue) : rawValue;
                }
              }
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

