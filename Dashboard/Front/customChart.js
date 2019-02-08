color1 = 'rgb(35, 137, 228, 1)'
color2 = 'rgb(0, 107, 166, 1)'
color3 = 'rgb(255, 188, 66, 1)'
color4 = 'rgb(241, 143, 1, 1)'
color5 = 'rgb(231, 50, 120, 1)'

var ctx = document.getElementById('myChart').getContext('2d');
var chart = new Chart(ctx, {
    // The type of chart we want to create
    type: 'line',

    // The data for our dataset
    data: {
        labels: ["January", "February", "March", "April", "May", "June", "July"],
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
            }
        ]
    },

    // Configuration options go here
    options: {}
});