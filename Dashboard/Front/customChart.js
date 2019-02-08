color1 = 'rgb(161,218,180)'
color2 = 'rgb(65,182,196)'
color3 = 'rgb(44,127,184)'
color4 = 'rgb(37,52,148)'

var ctx = document.getElementById('myChart').getContext('2d');
var chart = new Chart(ctx, {
    // The type of chart we want to create
    type: 'line',

    // The data for our dataset
    data: {
        labels: ["6:20", "6:30", "6:40", "6:50", "7:00", "7:10", "7:20"],
        datasets: [{
                label: "US East Coast",
                backgroundColor: color1,
                borderColor: color1,
                fill: false,
                data: [0, 10, 5, 2, 20, 30, 45],
            },
            {
                label: "US West Coast",
                backgroundColor: color2,
                borderColor: color2,
                fill: false,
                data: [5, 15, 55, 25, 25, 34, 45],
            },
            {
                label: "Asia Pacific",
                backgroundColor: color3,
                borderColor: color3,
                fill: false,
                data: [50, 150, 55, 255, 254, 342, 45],
            },
            {
                label: "South America",
                backgroundColor: color4,
                borderColor: color4,
                fill: false,
                data: [23, 15, 23, 28, 44, 120, 22],
            }
        ]
    },

    // Configuration options go here
    options: {}
});