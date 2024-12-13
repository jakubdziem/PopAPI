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

function renderChart(labels, data, label) {
  const ctx = document.getElementById('myChart').getContext('2d');
  new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: label,
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
            text: 'Week Start Date'
          }
        },
        y: {
          min: 0,
          title: {
            display: true,
            text: 'Number of Users'
          }
        }
      },
      plugins: {
        legend: {
          display: true
        }
      }
    }
  });
}

document.addEventListener('DOMContentLoaded', function () {
  async function fetchAndRenderChart() {
    const modeSelector = document.getElementById("modeSelect");
    const selectedMode = modeSelector.value;
    try {
      let statsData;
      const attributeSelect = document.getElementById("attributeSelect");
      if(attributeSelect.value === "newUsers") {
        const response = await fetch(`/stats_for_chart_new_users`);
        if (!response.ok) {
          throw new Error(`Error fetching stats: ${response.status}`);
        }
        statsData = await response.json();
      } else if(attributeSelect.value === "newUsersWeekly") {
        const response = await fetch(`/stats_for_chart_new_users_weekly`);
        if (!response.ok) {
          throw new Error(`Error fetching stats: ${response.status}`);
        }
        statsData = await response.json();
      } else if(attributeSelect.value === "activeUsers") {
        const response = await fetch(`/stats_for_chart_active_users`);
        if (!response.ok) {
          throw new Error(`Error fetching stats: ${response.status}`);
        }
        statsData = await response.json();
      } else if(attributeSelect.value === "activeUsersWeekly") {
        const response = await fetch(`/stats_for_chart_active_users_weekly`);
        if (!response.ok) {
          throw new Error(`Error fetching stats: ${response.status}`);
        }
        statsData = await response.json();
      } else {
        const response = await fetch(`/stats_for_chart/${selectedMode}`);
        if (!response.ok) {
          throw new Error(`Error fetching stats: ${response.status}`);
        }
        statsData = await response.json();
      }
      console.log("Stats Data:", statsData);
      const labels = (attributeSelect.value === "activeUsersWeekly" || attributeSelect.value === "newUsersWeekly")
          ? statsData.map(stat => stat.weekStartDate)
          : statsData.map(stat => stat.day);
      console.log(labels)
      let data;
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
        case "newUsers":
          data = statsData.map(stat => stat.numberOfUsers)
          break;
        case "newUsersWeekly":
          data = statsData.map(stat => stat.newUsers)
          break;
        case "activeUsers":
          data = statsData.map(stat => stat.activeUsers)
          break;
        case "activeUsersWeekly":
          data = statsData.map(stat => stat.activeUsers)
          break;
        default:
          data = statsData.map(stat => stat.totalGamePlayed);
          break;
      }
      console.log(data)


      if (labels.length === 0 || data.length === 0) {
        console.error('No valid data available to render the chart');
        return;
      }

      if(attributeSelect.value !== "activeUsersWeekly"){
        renderChart(labels, data, attributeSelect.value)
      } else {
        let summedActiveUsersData = statsData.map(stat => stat.activeUsers)
        let newActiveUsersData = statsData.map(stat => stat.activeNewUsers)
        let oldActiveUsersData = statsData.map(stat => stat.activeOldUsers)
        const ctx = document.getElementById('myChart').getContext('2d');
        new Chart(ctx, {
          type: 'line',
          data: {
            labels: labels,
            datasets: [
              {
                label: 'New Active Users',
                data: newActiveUsersData,
                borderColor: 'rgba(75, 192, 192, 1)',
                backgroundColor: 'rgba(75, 192, 192, 0.1)',
                borderWidth: 2,
                tension: 0.3,
              },
              {
                label: 'Old Active Users',
                data: oldActiveUsersData,
                borderColor: 'rgba(255, 99, 132, 1)',
                backgroundColor: 'rgba(255, 99, 132, 0.1)',
                borderWidth: 2,
                tension: 0.3,
              },
              {
                label: 'Summed Active Users',
                data: summedActiveUsersData,
                borderColor: 'rgba(54, 162, 235, 1)',
                backgroundColor: 'rgba(54, 162, 235, 0.1)',
                borderWidth: 2,
                tension: 0.3,
              }
            ]
          },
          options: {
            responsive: true,
            scales: {
              x: {
                title: {
                  display: true,
                  text: 'Week Start Date',
                }
              },
              y: {
                min: 0,
                title: {
                  display: true,
                  text: 'Number of Users',
                }
              }
            },
            plugins: {
              legend: {
                display: true,
                position: 'top',
              },
            }
          }
        });
      }
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

