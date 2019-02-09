color1 = 'rgb(161,218,180)'
color2 = 'rgb(65,182,196)'
color3 = 'rgb(44,127,184)'
color4 = 'rgb(37,52,148)'

var ctx = document.getElementById('myChart').getContext('2d');
var chart = new Chart(ctx, {
    // The type of chart we want to create
    type: 'line',

    // The data for our dataset
    data: { },

    // Configuration options go here
    options: {
        scales: {
            yAxes: [{
                scaleLabel: {
                    display: true,
                    labelString: 'Time in seconds'
                  }
            }],
            xAxes: [{
                scaleLabel: {
                    display: true,
                    labelString: 'Hour'
                  }
            }]
        }
    }
});

function removeData() {
    chart.data.labels.pop();
    chart.data.datasets.forEach((dataset) => {
        dataset.data.pop();
    });
    chart.update();
}

function addData(labels, datasets) {
    chart.data.labels = labels;
    chart.data.datasets = datasets;
    chart.update();
}